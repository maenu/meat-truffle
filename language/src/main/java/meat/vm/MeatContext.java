package meat.vm;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

// TODO could be bootstrapped
public class MeatContext extends MeatObject {

	private Optional<MeatObject> parent;
	private boolean isolated;
	private Map<Object, MeatObject> variables;

	public MeatContext(Optional<MeatObject> parent, boolean isolated) {
		super();
		this.parent = parent;
		this.isolated = isolated;
		this.variables = new HashMap<>();
		this.variables.put("context", new MeatVariable(this));
	}

	@Override
	protected MeatObject newOracle() {
		Map<String, BiFunction<MeatObject, MeatObject, MeatObject>> methods = new HashMap<>();
		methods.put("newContextBelow", (arguments, context) -> {
			return new MeatContext(Optional.of(context), false);
		});
		methods.put("newIsolatedContextBelow", (arguments, context) -> {
			return new MeatContext(Optional.of(context), true);
		});
		methods.put("isIsolated", (arguments, context) -> {
			return new MeatBoolean(this.isolated);
		});
		methods.put("hasParent", (arguments, context) -> {
			return new MeatBoolean(this.parent.isPresent());
		});
		methods.put("parent", (arguments, context) -> {
			return this.parent.orElse(this);
		});
		methods.put("includes:", (arguments, context) -> {
			MeatObject key = arguments.respondTo("at:",
					new MeatList(new MeatObject[] { new MeatNumber(new BigDecimal(1)) }), context);
			return new MeatBoolean(this.variables.containsKey(key.value()));
		});
		methods.put("owning:fallback:", (arguments, context) -> {
			MeatObject key = arguments.respondTo("at:",
					new MeatList(new MeatObject[] { new MeatNumber(new BigDecimal(1)) }), context);
			MeatObject includes = this.respondTo("includes:", new MeatList(new MeatObject[] { key }), context);
			return includes.respondTo("ifTrue:ifFalse:",
					new MeatList(new MeatObject[] { new MeatBlock(new String[0], (arguments_, context_) -> {
						return this;
					}), new MeatBlock(new String[0], (arguments_, context_) -> {
						MeatObject isolated = this.respondTo("isIsolated", new MeatList(new MeatObject[] {}), context_);
						return isolated.respondTo("ifTrue:ifFalse:", new MeatList(
								new MeatObject[] { new MeatBlock(new String[0], (arguments__, context__) -> {
									return this;
								}), new MeatBlock(new String[0], (arguments__, context__) -> {
									MeatObject fallback = arguments.respondTo("at:",
											new MeatList(new MeatObject[] { new MeatNumber(new BigDecimal(2)) }),
											context__);
									MeatObject hasParent = this.respondTo("hasParent",
											new MeatList(new MeatObject[] {}), context__);
									return hasParent.respondTo("ifTrue:ifFalse:", new MeatList(new MeatObject[] {
											new MeatBlock(new String[0], (arguments___, context___) -> {
												return this
														.respondTo("parent", new MeatList(new MeatObject[] {}),
																context___)
														.respondTo("owning:fallback:",
																new MeatList(new MeatObject[] { key, fallback }),
																context___);
											}), new MeatBlock(new String[0], (arguments___, context___) -> {
												return fallback;
											}) }), context__);
								}) }), context_);
					}) }), context);
		});
		methods.put("owning:", (arguments, context) -> {
			MeatObject key = arguments.respondTo("at:",
					new MeatList(new MeatObject[] { new MeatNumber(new BigDecimal(1)) }), context);
			return this.respondTo("owning:fallback:", new MeatList(new MeatObject[] { key, this }), context);
		});
		methods.put("atSelf:", (arguments, context) -> {
			MeatObject key = arguments.respondTo("at:",
					new MeatList(new MeatObject[] { new MeatNumber(new BigDecimal(1)) }), context);
			MeatObject includes = this.respondTo("includes:", new MeatList(new MeatObject[] { key }), context);
			return includes.respondTo("ifTrue:ifFalse:",
					new MeatList(new MeatObject[] { new MeatBlock(new String[0], (arguments_, context_) -> {
						return this.variables.get(key.value());
					}), new MeatBlock(new String[0], (arguments_, context_) -> {
						MeatObject variable = new MeatVariable(new MeatObject());
						this.variables.put(key.value(), variable);
						return variable;
					}) }), context);
		});
		methods.put("at:", (arguments, context) -> {
			MeatObject key = arguments.respondTo("at:",
					new MeatList(new MeatObject[] { new MeatNumber(new BigDecimal(1)) }), context);
			MeatObject owningObject = this.respondTo("owning:", new MeatList(new MeatObject[] { key }), context);
			return owningObject.respondTo("atSelf:", new MeatList(new MeatObject[] { key }), context);
		});
		methods.put("atSelf:put:", (arguments, context) -> {
			MeatObject key = arguments.respondTo("at:",
					new MeatList(new MeatObject[] { new MeatNumber(new BigDecimal(1)) }), context);
			MeatObject value = arguments.respondTo("at:",
					new MeatList(new MeatObject[] { new MeatNumber(new BigDecimal(2)) }), context);
			if (!(value instanceof MeatVariable)) {
				value = new MeatVariable(value);
			}
			this.variables.put(key.value(), value);
			return value;
		});
		methods.put("at:put:", (arguments, context) -> {
			MeatObject key = arguments.respondTo("at:",
					new MeatList(new MeatObject[] { new MeatNumber(new BigDecimal(1)) }), context);
			MeatObject value = arguments.respondTo("at:",
					new MeatList(new MeatObject[] { new MeatNumber(new BigDecimal(2)) }), context);
			MeatObject owningObject = this.respondTo("owning:", new MeatList(new MeatObject[] { key }), context);
			return owningObject.respondTo("atSelf:put:", new MeatList(new MeatObject[] { key, value }), context);
		});
		return new MeatOracle(this, methods);
	}

	@Override
	public String toString() {
		return String.format("%b{%s}%b", this.isolated,
				String.join(", ", this.variables.entrySet().stream().map(
						e -> String.format("%s=%s", e.getKey(), e.getValue().value() == this ? "self" : e.getValue()))
						.collect(Collectors.toList())),
				this.parent.isPresent());
	}

}
