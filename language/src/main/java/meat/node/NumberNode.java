package meat.node;

import java.math.BigDecimal;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.source.SourceSection;

import meat.Language;
import meat.vm.MeatNumber;
import meat.vm.MeatObject;

public class NumberNode extends MeatNode {

	private final BigDecimal value;

	public NumberNode(Language language, FrameDescriptor frameDescriptor, SourceSection sourceSection,
			BigDecimal value) {
		super(language, frameDescriptor, sourceSection);
		this.value = value;
	}

	@Override
	public MeatObject execute(VirtualFrame frame, MeatObject context, MeatObject[] arguments) {
		return new MeatNumber(this.value);
	}

}
