package mpaugreen.keycloak.requiredaction;

import org.keycloak.authentication.InitiatedActionSupport;
import org.keycloak.authentication.RequiredActionContext;
import org.keycloak.authentication.RequiredActionProvider;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.models.GroupModel;

import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.function.Consumer;



/**
 * @author Mriganka Paul
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
		if (!(isGroupApproved(context)  || isUserApproved(context))) {
			context.getUser().addRequiredAction(PROVIDER_ID);
		}
	}

	@Override
	public void requiredActionChallenge(RequiredActionContext context) {
		if (!(isGroupApproved(context)  || isUserApproved(context))) {
			context.challenge(createForm(context, null));
		} else {
			context.success();
		}
	}

	private boolean isUserApproved(RequiredActionContext context) {
		return context.getUser().getFirstAttribute(ADMIN_APPROVE_FIELD) != null;
	}

	private boolean isGroupApproved(RequiredActionContext context) {
		Optional<GroupModel> gm = context.getUser()
				.getGroupsStream()
				.parallel()
				.filter(groupModel -> groupModel.getFirstAttribute(ADMIN_APPROVE_FIELD) != null)
				.findAny();

		return gm.isEmpty();
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
