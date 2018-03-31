package meat;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.instrumentation.ProvidedTags;
import com.oracle.truffle.api.nodes.RootNode;
import com.oracle.truffle.api.source.Source;

import meat.vm.MeatObject;

@TruffleLanguage.Registration(id = Language.ID, name = Language.NAME, version = Language.VERSION, mimeType = Language.MIME_TYPE)
@ProvidedTags({ /* FIXME which tags? */ })
public final class Language extends TruffleLanguage<Context> {

	public static final String ID = "meat";
	public static final String NAME = "meat";
	public static final String VERSION = "0.1.0";
	public static final String MIME_TYPE = "application/x-meat";

	@Override
	protected Context createContext(Env env) {
		return new Context();
	}

	@Override
	protected Object getLanguageGlobal(Context context) {
		return context;
	}

	@Override
	protected boolean isObjectOfLanguage(Object object) {
		return object instanceof MeatObject;
	}

	@Override
	protected CallTarget parse(ParsingRequest request) throws Exception {
		Source source = request.getSource();
		RootNode root = Parser.parse(this, source);
		return Truffle.getRuntime().createCallTarget(root);
	}

}
