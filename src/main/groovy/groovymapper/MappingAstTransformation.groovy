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
//        println "CALLED $astNodes"
        if (!astNodes) return
        if (!astNodes[0]) return
        if (!astNodes[1]) return
        if (!(astNodes[0] instanceof AnnotationNode)) return
        if (astNodes[0].classNode?.name != MappingAst.name) return
        if (!(astNodes[1] instanceof MethodNode)) return

        MethodNode method = astNodes[1]

        def clos = method.getCode().statements[0].expression.arguments[2]
//        println "CODE: $clos"

        def param = clos.parameters[0]
        def closs = clos.code.statements

        // TODO auto-discover fields
        def assignment = createAssignment(param, "name")
//        println "CODE: $assignment"
        closs.add(0, assignment)

        assignment = createAssignment(param, "color")
//        println "CODE: $assignment"
        closs.add(0, assignment)
    }

    private Statement createAssignment(Parameter param, String field) {
        new AstBuilder().buildFromSpec {
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