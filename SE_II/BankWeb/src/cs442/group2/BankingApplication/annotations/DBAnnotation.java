package cs442.group2.BankingApplication.annotations;

import java.lang.annotation.*;


@Target(ElementType.LOCAL_VARIABLE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DBAnnotation {
	String[]  variable () default {};
	String[]  table    () default {};
	String[]  column   () default {};
	boolean[] isSource () default {};
}
