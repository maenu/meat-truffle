package meat.node;

import java.util.HashMap;
import java.util.Map;

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
		Map<Object, MeatObject> values = new HashMap<>();
		for (int i = 0; i < this.entries.length; i++) {
			Pair<MeatNode, MeatNode> entry = this.entries[i];
			// FIXME should this really be the value?
			Object key = entry.getLeft().execute(frame, context, arguments).value();
			MeatObject value = entry.getRight().execute(frame, context, arguments);
			values.put(key, value);
		}
		return new MeatDictionary(values);
	}

}
