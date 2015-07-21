package groovymapper

import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.builder.AstBuilder
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codehaus.groovy.ast.stmt.Statement
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

@GroovyASTTransformation(phase = CompilePhase.SEMANTIC_ANALYSIS)
class MappingAstTransformation implements ASTTransformation {

    void visit(ASTNode[] astNodes, SourceUnit sourceUnit) {
        // println "CALLED $astNodes"
        if (!astNodes) return
        if (!astNodes[0]) return
        if (!astNodes[1]) return
        if (!(astNodes[0] instanceof AnnotationNode)) return
        if (astNodes[0].classNode?.name != MappingAst.name) return
        if (!(astNodes[1] instanceof MethodNode)) return

        MethodNode method = astNodes[1]

        def signature = method.getCode().statements[0].expression
        // println signature

        // discover source class
        def sourceType = signature.objectExpression.type
        // println "SOURCE CLASS: $sourceType"

        // discover target class
        def targetType = signature.arguments[0].type
        // println "TARGET CLASS: $targetType"

        // discover the closure (last parameter from map method)
        def clos = signature.arguments[2]
        // println "CODE: $clos"

        // actual code statements in closure
        def closs = clos.code.statements

        // closure parameter = source object
        def sourceParam = clos.parameters[0]

        // discover which properties can be auto-mapped
        def sourceMap = sourceType.properties.collectEntries { [it.name, it.type.name] }
        // println "SOURCEMAP: $sourceMap"
        def params = targetType.properties.findAll { sourceMap[it.name] == it.type.name }.collect { it.name }
        // println "AUTO-MAPPABLE: $params"

        params.each {
            def assignment = createAssignment(sourceParam, it)
            // println "CODE: $assignment"
            closs.add(0, assignment)
        }
    }

    // assignment in format 'field = p.field' where p is closure param name
    private Statement createAssignment(Parameter param, String field) {
        (Statement) new AstBuilder().buildFromSpec {
            expression {
                binary {
                    expression << new VariableExpression(new DynamicVariable(field, false))
                    token "="
                    property {
                        expression << new VariableExpression(param)
                        constant field
                    }
                }
            }
        }[0]
    }
}