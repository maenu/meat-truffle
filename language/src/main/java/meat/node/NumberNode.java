package meat.node;

import java.math.BigDecimal;

import com.oracle.truffle.api.source.SourceSection;

import meat.vm.MeatNumber;
import meat.vm.MeatObject;

public class NumberNode extends MeatNode {

	private final BigDecimal value;

	public NumberNode(SourceSection sourceSection, BigDecimal value) {
		super(sourceSection);
		this.value = value;
	}

	@Override
	public MeatObject execute(MeatObject context) {
		return new MeatNumber(this.value);
	}

}
