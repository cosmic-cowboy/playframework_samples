
import java.util.List;

import com.avaje.ebean.Ebean;

import models.User;
import play.Application;
import play.GlobalSettings;
import play.libs.Yaml;


public class Global extends GlobalSettings{

	@Override
	public void onStart(Application app) {
		// タスクデータが空の場合、ymlからデータをインポートする
		if(User.find.findRowCount() == 0){
			Ebean.save((List) Yaml.load("initial-data.yml"));
		}
	}
}
