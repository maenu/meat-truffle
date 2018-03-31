package meat.node;

import com.oracle.truffle.api.source.SourceSection;

import meat.vm.MeatObject;
import meat.vm.MeatString;

public class StringNode extends MeatNode {

	private final String value;

	public StringNode(SourceSection sourceSection, String value) {
		super(sourceSection);
		this.value = value;
	}

	@Override
	public MeatObject execute(MeatObject context) {
		return new MeatString(this.value);
	}

}
