package leave;

import java.util.HashMap;
import java.util.Map;

/**
 * User：ThinerZQ
 * Email：thinerzq@gmail.com
 * Date：2017/5/2 11:32
 * Project：SCXML
 * Package：leave
 */
public class Resource {

    private static Map<String,Employe> resources= new HashMap();
    private static String employe ="employe";
    private static String DepartmentManager ="departmentManager";
    private static String PersonalManager ="personalManager";
    static {
        Employe employe1 = new Employe();

        Employe departmentManager = new Employe();

        Employe personalManager = new Employe();

        resources.put(employe1.position,employe1);
        resources.put(departmentManager.position,departmentManager);
        resources.put(personalManager.position,personalManager);
    }
    public Resource(){

    }
    public static Employe getEmploye(String position){

        return resources.get(position);
    }

}
