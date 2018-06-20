package gmail.chorman64.gac14.basic.util.function;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class ConditionalConsumer<T> implements Consumer<T> {
	private Predicate<T> condition;
	private Consumer<T> then = t->{};
	private Consumer<T> _else = t->{};
	private ConditionalConsumer(Predicate<T> condition) {
		this.condition = condition;
	}

	public static <T> ConditionalConsumer<T> If(Predicate<T> condition){
		return new ConditionalConsumer<>(condition);
	}
	public ConditionalConsumer<T> then(Consumer<T> then){
		this.then = then;
		return this;
	}
	public ConditionalConsumer<T> Else(Consumer<T> _else){
		this._else = _else;
		return this;
	}

	@Override
	public void accept(T t) {
		if(condition.test(t))
			then.accept(t);
		else
			_else.accept(t);
	}

}
