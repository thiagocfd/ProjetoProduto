import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class ConexaoBanco {
	
	private Connection connection=null;
	private Statement statement=null;
	//private ResultSet resultset=null;
	
	public Connection conectar() {
		
		String servidor="jdbc:mysql://localhost:3306/db_pedido";
		String usuario="root";
		String senha="admin123";
		String driver="com.mysql.cj.jdbc.Driver";
		
		try {
			Class.forName(driver);
			this.connection=DriverManager.getConnection(servidor,usuario,senha);
			this.statement=this.connection.createStatement();
		}
		catch(Exception e)
		{
			connection=null;
		}
		
		return connection;
	}
}
