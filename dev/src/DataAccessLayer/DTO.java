package DataAccessLayer;

public abstract class DTO<T extends DAO>
{
//    public final String IDColumnName = "ID";
    protected T controller;
    public DTO(T controller)
    {
        this.controller = controller;
    }
   /* public int getId(Object[] primaryKey){
        int x=0;
        for(Object field: primaryKey){
            x += field.hashCode();
        }
        this.id = x;
        return id;
    }*/


}
