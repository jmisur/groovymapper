package groovymapper

import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FirstParam

class MappingExtension {
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
