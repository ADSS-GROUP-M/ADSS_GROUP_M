package DataAccessLayer;

public abstract class DTO<T extends DAO>
{
//    public final String IDColumnName = "ID";
    protected T controller;
    private int id;
    private static int idCounter = 0;
    public DTO(T controller)
    {
        this.controller = controller;
        idCounter++;
        this.id = idCounter;


    }
   /* public int getId(Object[] primaryKey){
        int x=0;
        for(Object field: primaryKey){
            x += field.hashCode();
        }
        this.id = x;
        return id;
    }*/

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

    /*public boolean Delete()
    {
        return controller.delete(this);
    }*/

}
