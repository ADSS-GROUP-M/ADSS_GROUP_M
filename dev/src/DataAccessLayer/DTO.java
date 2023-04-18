package DataAccessLayer;

public abstract class DTO
{
    public final string IDColumnName = "ID";
    protected DAO _controller;
    private int Id { get; set; }
    protected DTO(DAO controller)
    {
        _controller = controller;
    }

    public boolean Delete()
    {
        return _controller.delete(this);
    }

}
