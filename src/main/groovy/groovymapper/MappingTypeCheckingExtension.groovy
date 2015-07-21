package groovymapper

import org.codehaus.groovy.ast.expr.BinaryExpression
import org.codehaus.groovy.ast.expr.ClosureExpression
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.ExpressionStatement
import org.codehaus.groovy.transform.stc.GroovyTypeCheckingExtensionSupport

class MappingTypeCheckingExtension extends GroovyTypeCheckingExtensionSupport.TypeCheckingDSL {

	def run() {
		methodNotFound { receiver, name, argList, argTypes, call ->

		}

		unresolvedVariable { var ->
			def scope = getCurrentScope()
//			println "VAR: $var in scope of ${scope}"
//			if (scope.name == 'mapping') {
//				scope.vars << var.name
//				handled = true
//			}
		}

		unresolvedProperty { pexp ->
//			println "PROP: " + pexp
		}

		unresolvedAttribute { aex ->
//			println "ARR: " + aex
		}

		beforeMethodCall { call ->
//			print "BEFORE METHOD CALL: "
			switch (call) {
				case org.codehaus.groovy.ast.expr.ConstructorCallExpression:
//					println "new ${call.type.name}()"
					break
				case org.codehaus.groovy.ast.expr.MethodCallExpression:
//					println "$call.method.value"
//					if (call.method.value == "map") {
//						newScope {
//							name = 'mapping'
//							vars = []
//						}
//						println "ENTERING SCOPE ${getCurrentScope()}"
//					}
					break
				default:
//					println call
					break;
			}
		}

		afterMethodCall { call ->
//			print "AFTER METHOD CALL: "
			switch (call) {
				case org.codehaus.groovy.ast.expr.ConstructorCallExpression:
//					println "new ${call.type.name}()"
					break
				case org.codehaus.groovy.ast.expr.MethodCallExpression:
//					println "$call.method.value"
//					if (call.method.value == "map") {
//						println "EXITING SCOPE: ${getCurrentScope()}"
//						scopeExit {
//							vars.each { addStaticTypeError("The variable [$it] is undeclared", call) }
//						}
//					}
					break
				default:
//					println call
					break
			}
		}

		onMethodSelection { call, node ->
//			print "ON METHOD SELECTION:"

			switch (call) {
				case org.codehaus.groovy.ast.expr.ConstructorCallExpression:
//					println "new ${call.type.name}()"
					break
				case org.codehaus.groovy.ast.expr.MethodCallExpression:
//					println "$call.method.value"
					if (call.method.value == "map") {
						def props = []

						// check closure
						def closure = call.arguments[-1]
						if (closure instanceof ClosureExpression) {
							// assert param is source
//							println closure.parameters*.type.name
							def code = closure.code
							if (code instanceof BlockStatement) {
								code.statements.each {
									if (it instanceof ExpressionStatement) {
										def es = it.expression
										if (es instanceof BinaryExpression) {
											def var = es.leftExpression
											if (var instanceof VariableExpression) {
												props << var.variable
											}
										}
									}
								}
//								println "CAPTURED VARS: $props"
							}
						}
//                        println "CALL $call"

						def source = call.receiver.type
						def target = call.arguments[0].type

//						println "MAPPING $source.name -> $target.name"

						def sourceMap = source.fields.collectEntries { [it.name, it.type.name] }
//						println "Source fields: $sourceMap"

						def targetMiss = target.fields.findAll { (!sourceMap[it.name] || sourceMap[it.name] != it.type.name) && !it.name.startsWith('__timeStamp') }.collect { it.name }
//						println "A miss: $targetMiss"

						def targetFields = target.fields.collect { it.name }
						def exclusions = call.arguments[1].expressions*.value

//						println targetFields
						def novars = (exclusions - targetFields)
//						println "Novars: $novars"

						novars.each { addStaticTypeError("The variable [$it] is undeclared", call) }

						def unmapped = targetMiss - props - exclusions
//						println "unmapped: $unmapped"
						if (unmapped)
							addStaticTypeError("Missing mapping for fields $unmapped", call.method)


					}
					break
				default:
                    //println call
                    break;
			}
		}

		beforeVisitMethod { methodNode ->
//			println "BEFORE VISIT METHOD: " + methodNode.name
		}

		afterVisitMethod { methodNode ->
//			println "AFTER VISIT METHOD: " + methodNode.name
		}
	}
}

