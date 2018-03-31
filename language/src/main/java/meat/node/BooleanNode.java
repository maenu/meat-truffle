package meat.node;

import com.oracle.truffle.api.source.SourceSection;

import meat.vm.MeatBoolean;
import meat.vm.MeatObject;

public class BooleanNode extends MeatNode {

	private final Boolean value;

	public BooleanNode(SourceSection sourceSection, Boolean value) {
		super(sourceSection);
		this.value = value;
	}

	@Override
	public MeatObject execute(MeatObject context) {
		return new MeatBoolean(this.value);
	}

}
