package meat.node;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.graalvm.collections.Pair;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.source.SourceSection;

import meat.Language;
import meat.vm.MeatDictionary;
import meat.vm.MeatObject;

public class DictionaryNode extends MeatNode {

	// FIXME @Children
	private final Pair<MeatNode, MeatNode>[] entries;

	public DictionaryNode(Language language, FrameDescriptor frameDescriptor, SourceSection sourceSection,
			Pair<MeatNode, MeatNode>[] entries) {
		super(language, frameDescriptor, sourceSection);
		this.entries = entries;
	}

	@Override
	public MeatObject execute(VirtualFrame frame, MeatObject context, MeatObject[] arguments) {
		Map<Object, MeatObject> values = Stream.of(this.entries).sequential()
				.collect(Collectors.toMap(p -> p.getLeft().execute(frame, context, arguments).value(),
						p -> p.getRight().execute(frame, context, arguments)));
		return new MeatDictionary(values);
	}

}
