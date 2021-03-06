package controllers;

import models.*;
import play.mvc.Result;
import play.mvc.Security;
import play.mvc.Http.Context;

public class Secured extends Security.Authenticator{

	/* (非 Javadoc)
	 * @see play.mvc.Security.Authenticator#getUsername(play.mvc.Http.Context)
	 * 現在ログインしているユーザ名を取得
	 * ログイン時にセッションに設定したemailを見てログインしたが判定している
	 */
	@Override
	public String getUsername(Context ctx) {
		return ctx.session().get("email");
	}
	
	/* (非 Javadoc)
	 * @see play.mvc.Security.Authenticator#onUnauthorized(play.mvc.Http.Context)
	 * 
	 * 認証処理に失敗した際の動作を設定する
	 * ここではログイン画面に遷移する
	 */
	@Override
	public Result onUnauthorized(Context arg0) {
		return redirect(routes.Application.login());
	}
	
	/**
	 * リクエストの発行者がプロジェクトに参加しているか
	 * @param projectId
	 * @return
	 */
	public static boolean isMemberOf(Long projectId){
		// Context.current()
		// アクションの中にいない場合でも、この便利な方法でリクエストにアクセスすることができる
		String ownerEmail = Context.current().request().username();
		return Project.isMember(projectId, ownerEmail);
	}
	
	public static boolean isOwnerOf(Long taskId){
		String ownerEmail = Context.current().request().username();
		return Task.isOwnerOf(taskId, ownerEmail);
	}
}
