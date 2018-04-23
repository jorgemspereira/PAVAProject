package ist.meic.pa.GenericFunctionsExtended;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Combination {
    CombinationType type();
    CombinationOrder order() default CombinationOrder.LEAST_TO_MOST;
}

