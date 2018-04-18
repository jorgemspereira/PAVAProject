package ist.meic.pa.GenericFunctionsExtended;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Combination {
    CombinationType type();
    CombinationOrder order() default CombinationOrder.LEAST_TO_MOST;
}

