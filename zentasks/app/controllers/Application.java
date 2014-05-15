package controllers;

import models.Project;
import models.Task;
import models.User;
import play.*;
import play.data.Form;
import play.mvc.*;
import views.html.*;
import views.html.helper.form;

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
    public static Result index() {
        return ok(index.render(
        		Project.find.all(),
        		Task.find.all()
        ));
    }
}
