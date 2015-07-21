package groovymapper

import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FirstParam

class MappingExtension {
    /**
     * Maps source object properties to target object properties, with customizable assignment
     * available via closure
     *
     * @param source object from which we want to get the values
     * @param target object to which we want map values
     * @param exluded excluded properties which we don't want to map
     * @param closure customization closure to map properties explicitely, with target as
     * delegate and source as parameter
     * @return target with mapped properties
     */
    static <S, T> T map(S source,
                        @DelegatesTo.Target T target,
                        List<String> exluded,
                        @ClosureParams(FirstParam) @DelegatesTo Closure closure = null) {

        Closure clone = closure.clone()
        clone.delegate = target
        clone(source)
        target
    }
}
