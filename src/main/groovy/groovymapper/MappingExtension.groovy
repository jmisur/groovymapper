package groovymapper

import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FirstParam

import java.lang.reflect.Field

class MappingExtension {
    static <S, T> T map(S source, @DelegatesTo.Target T target, List<String> exluded, @ClosureParams(FirstParam) @DelegatesTo Closure closure = null) {

//   		Map<String, Field> sourceFields = source.class.declaredFields.findAll { !it.synthetic }.collectEntries { [(it.name): it] }
//
//        target.class.declaredFields.findAll { !it.synthetic }.each { targetField ->
//            def sourceField = sourceFields[targetField.name]
//            if (!sourceField || sourceField.type != targetField.type) return
//
//            target[targetField.name] = source[targetField.name]
//        }

        Closure clone = closure.clone()
        clone.delegate = target
        clone(source)

		target
	}

}
