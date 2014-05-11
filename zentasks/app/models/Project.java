package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import play.db.ebean.Model;

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
}
