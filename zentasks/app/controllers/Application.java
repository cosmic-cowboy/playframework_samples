package controllers;

import models.Project;
import models.Task;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.index;
import views.html.login;

public class Application extends Controller {

	
    /**************
     * ログイン画面　*
     **************/
	/**
	 * ログイン用フォーム
	 */
	public static class Login {
		public String email;
		public String password;
		
		public String validate(){
			if(User.authenticate(email, password) == null){
				return "メールアドレス、もしくは、パスワードが正しくありません";
			}
			return null;
		}
	}
	
	/**
     * ログイン画面のアクション
     * @return
     */
	public static Result login() {
    	Form<Login> f = new Form(Login.class);
        return ok(login.render(f));
    }
    
    /**
     * ログイン画面の認証処理
     * @return
     */
    public static Result authenticate(){
    	Form<Login> loginForm = new Form(Login.class).bindFromRequest();
    	if(loginForm.hasErrors()){
    		return badRequest(login.render(loginForm));
    	} else {
    		session().clear();
    		session("email", loginForm.get().email);
    		return redirect(routes.Application.index());
    	}
    }
    /**************
     * トップページ　*
     **************/
    /**
     * トップページのアクション
     * @return
     */
	@Security.Authenticated(Secured.class)
    public static Result index() {
        return ok(index.render(
        		Project.find.all(),
        		Task.find.all(),
        		User.find.byId(request().username())
        ));
    }
	
    /****************
     * ログアウト画面　*
     ****************/
	public static Result logout() {
		session().clear();
		flash("success","You're been logged out");
		return redirect(routes.Application.login());
	}
}
