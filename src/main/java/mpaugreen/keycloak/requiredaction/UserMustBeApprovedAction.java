package mpaugreen.keycloak.requiredaction;

import org.keycloak.authentication.InitiatedActionSupport;
import org.keycloak.authentication.RequiredActionContext;
import org.keycloak.authentication.RequiredActionProvider;
import org.keycloak.forms.login.LoginFormsProvider;
import javax.ws.rs.core.Response;
import java.util.function.Consumer;

/**
 * @author Niko Köbler, https://www.n-k.de, @dasniko
 */
public class UserMustBeApprovedAction implements RequiredActionProvider {

	public static final String PROVIDER_ID = "USER_MUST_BE_APPROVED";

	private static final String ADMIN_APPROVE_FIELD = "x-approved";

	@Override
	public InitiatedActionSupport initiatedActionSupport() {
		return InitiatedActionSupport.SUPPORTED;
	}

	@Override
	public void evaluateTriggers(RequiredActionContext context) {
		if (context.getUser().getFirstAttribute(ADMIN_APPROVE_FIELD) == null) {
			context.getUser().addRequiredAction(PROVIDER_ID);
		}
	}

	@Override
	public void requiredActionChallenge(RequiredActionContext context) {
		if (context.getUser().getFirstAttribute(ADMIN_APPROVE_FIELD) == null) {
			context.challenge(createForm(context, null));
		} else {
			context.success();
		}
	}

	@Override
	public void processAction(RequiredActionContext context) {}

	@Override
	public void close() {
	}

	private Response createForm(RequiredActionContext context, Consumer<LoginFormsProvider> formConsumer) {
		LoginFormsProvider form = context.form();
		return form.createForm("adminapprove.ftl");
	}

}
