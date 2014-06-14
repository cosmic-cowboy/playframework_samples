package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.avaje.ebean.Ebean;

import play.db.ebean.Model;
import play.mvc.Content;

@Entity
public class Project extends Model {

	private static final long serialVersionUID = 1L;
	
	@Id
	public Long id;
	public String name;
	public String folder;
	@ManyToMany(cascade = CascadeType.REMOVE)
	public List<User> members = new ArrayList<User>();

	public Project(String name, String folder, User owner) {
		this.name = name;
		this.folder = folder;
		this.members.add(owner);
	}
	
	public static Finder<Long, Project> find = 
			new Finder<Long, Project>(Long.class, Project.class);
	
	/**
	 * @param name
	 * @param folder
	 * @param ownerEmail
	 * @return
	 */
	public static Project create(String name, String folder, String ownerEmail){
		// Retrieves an entity reference
		Project project = new Project(name, folder, User.find.ref(ownerEmail));
		project.save();
		// members の多対多の関連は明示的に保存する
		project.saveManyToManyAssociations("members");
		return project;
	}
	
	/**
	 * ユーザが所属しているプロジェクト一覧を取得
	 * @param ownerEmail
	 * @return
	 */
	public static List<Project> findInvolving(String ownerEmail){
		// members リスト中の User の email プロパティを参照している
		return find.where()
				.eq("members.email", ownerEmail)
				.findList();
	}
	
	/**
	 * ユーザがプロジェクトに参加しているか
	 * @param projectId:プロジェクトID
	 * @param ownerEmail:ユーザのEmail
	 * @return
	 */
	public static boolean isMember(Long projectId, String ownerEmail){
		return find.where()
				.eq("members.email", ownerEmail)
				.eq("id", projectId)
				.findRowCount() > 0;
	}
	
	/**
	 * プロジェクトの名称を変更する
	 * @param projectId:プロジェクトID
	 * @param newName:プロジェクト名
	 * @return
	 */
	public static String rename(Long projectId, String newName){
		Project project = find.ref(projectId);
		project.name = newName;
		project.update();
		return newName;
	}

	/**
	 * プロジェクトメンバーを削除する
	 * @param projectId:プロジェクトID
	 * @param username:ユーザ名
	 */
	public static void removeMember(Long projectId, String username) {
        Project project = Project.find.setId(projectId).fetch("members", "email").findUnique();
        project.members.remove(User.find.ref(username));
        project.saveManyToManyAssociations("members");
		
	}

	/**
	 * プロジェクトメンバーを追加する
	 * @param projectId:プロジェクトID
	 * @param username:ユーザ名
	 */
	public static void addMember(Long projectId, String username) {
        Project project = Project.find.setId(projectId).fetch("members", "email").findUnique();
        project.members.add(User.find.ref(username));
        project.saveManyToManyAssociations("members");
		
	}

	/**
	 * @param group
	 * @param newName
	 * @return
	 */
	public static String renameFolder(String group, String newName) {
		Ebean.createSqlUpdate(
				"update project set folder= :newName where folder= :folder"
			)
			.setParameter("newName", newName)
			.setParameter("folder", group)
			.execute();
		return newName;
	}

	/**
	 * グループに含まれるプロジェクトをすべて削除する
	 * @param group
	 */
	public static void deleteInFolder(String group) {
		Ebean.createSqlUpdate(
			"delete from project where folder= :folder"
		)
		.setParameter("folder", group)
		.execute();
	}
}
