package dbbindings;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
public @interface DBField {
	public String Name() default "";
	public boolean PrimaryKey() default false;
	public String ForeignKey() default "";
	public boolean One2Many() default false;
}
