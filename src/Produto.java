import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;

public class Produto extends JFrame {

	private JTextField txtID;
	private JTextField txtDtCadastro;
	private JTextField txtDescricao;
	private JTable tabProduto;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Produto frame = new Produto();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Produto() {
		
		setTitle("PRODUTOS");
		setFont(new Font("Dialog", Font.BOLD, 13));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 200, 700, 350);
		getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("ID");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel.setBounds(26, 25, 46, 14);
		getContentPane().add(lblNewLabel);
		
		txtID = new JTextField();
		txtID.setEditable(false);
		txtID.setBounds(46, 23, 46, 20);
		getContentPane().add(txtID);
		txtID.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Data Cadastro");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_1.setBounds(104, 26, 83, 14);
		getContentPane().add(lblNewLabel_1);
		
		txtDtCadastro = new JTextField();
		txtDtCadastro.setEditable(false);
		txtDtCadastro.setBounds(197, 23, 126, 20);
		getContentPane().add(txtDtCadastro);
		txtDtCadastro.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Descricao");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_2.setBounds(26, 67, 60, 14);
		getContentPane().add(lblNewLabel_2);
		
		txtDescricao = new JTextField();
		txtDescricao.setEditable(false);
		txtDescricao.setBounds(96, 65, 206, 20);
		getContentPane().add(txtDescricao);
		txtDescricao.setColumns(10);
		
		JButton btnNovo = new JButton("Novo");
		btnNovo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtDescricao.setEditable(true);
				txtDescricao.setText(null);
				txtID.setText(null);
				txtDtCadastro.setText(null);
				txtDescricao.requestFocus();
			}
		});
		btnNovo.setBounds(312, 64, 65, 23);
		getContentPane().add(btnNovo);
		
		JButton btnGravar = new JButton("Gravar");
		btnGravar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					gravar();
				}
				catch(Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnGravar.setBounds(387, 64, 82, 23);
		getContentPane().add(btnGravar);
		
		JButton btnAtualizar = new JButton("Atualizar");
		btnAtualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					atualizar();
				}
				catch(Exception e3) {
					e3.printStackTrace();
				}
			}
		});
		btnAtualizar.setBounds(479, 64, 91, 23);
		getContentPane().add(btnAtualizar);
		
		JButton btnExcluir = new JButton("Excluir");
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					excluir();
				}
				catch(Exception e2) {
					e2.printStackTrace();
				}
			}
		});
		btnExcluir.setBounds(580, 64, 83, 23);
		getContentPane().add(btnExcluir);
		
		tabProduto = new JTable();
		tabProduto.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				linhaSelecionada();
				
			}
		});
		
		tabProduto.setBounds(15, 99, 648, 201);
        getContentPane().add(tabProduto);
        
		try {
			listarProduto();
		}
		catch(Exception ex) {
			double x=0;
		}
	}
	
	private void linhaSelecionada() {
		
		desabilitarText();
		
		DefaultTableModel tableModel = (DefaultTableModel) tabProduto.getModel();
		
		int row = tabProduto.getSelectedRow();
		
		if (tableModel.getValueAt(row, 0).toString()!="ID") {
			
			txtID.setText(tableModel.getValueAt(row, 0).toString());
			txtDescricao.setText(tableModel.getValueAt(row, 1).toString());
			txtDtCadastro.setText(tableModel.getValueAt(row, 2).toString());
		}
	};
	
	private void desabilitarText() {
		txtDescricao.setEditable(false);
		txtID.setEditable(false);
		txtDtCadastro.setEditable(false);
	}
		
	private void listarProduto() throws SQLException {
		Connection con = null;
		ConexaoBanco objconexao = new ConexaoBanco();
		con=objconexao.conectar();
		
		if(con == null) {
			JOptionPane.showMessageDialog(null,"Conexão não realizada");
		}
		else {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM db_pedido.produto");
			
			String[] colunasTabela = new String[]{ "ID", "Descrição", "Pontuação" };
			DefaultTableModel modeloTabela = new DefaultTableModel(null,colunasTabela);
			modeloTabela.addRow(new String[] {"ID", "DESCRIÇÃO", "CADASTRO"});
			
			if(rs != null) {
				while(rs.next()) {
					modeloTabela.addRow(new String[] {
						String.valueOf(rs.getInt("ID")),
						rs.getString("descricao"),
						rs.getString("data_cadastro")
					});
				}
			}
			
			tabProduto.setModel(modeloTabela);
		}
	}
	
	private void gravar() throws SQLException {
		Connection con = null;
		ConexaoBanco objconexao = new ConexaoBanco();
		
		try {
			con = objconexao.conectar();
			
			if(con == null) {
				JOptionPane.showMessageDialog(null, "Conexao não realizada!");
			}
			else {
				Statement stmt = con.createStatement();
				String query="insert into db_pedido.produto(descricao) values('"+txtDescricao.getText()+"')";
				stmt.executeUpdate(query);
				listarProduto();
				txtDescricao.setText(null);
				desabilitarText();
			}	
		}
		catch(Exception ex) {
			con.close();
			JOptionPane.showMessageDialog(null, "Não foi possível gravar. "+ex.getMessage());
		}
	}
	
	private void excluir() throws SQLException {
		Connection con = null;
		ConexaoBanco objconexao = new ConexaoBanco();
		
		try {
			con = objconexao.conectar();
			
			if(con == null) {
				JOptionPane.showMessageDialog(null, "Conexao não realizada!");
			}
			else {
				Statement stmt = con.createStatement();
				String query="delete from db_pedido.produto where id = "+txtID.getText();
				stmt.executeUpdate(query);
				listarProduto();
				desabilitarText();
			}	
		}
		catch(Exception ex) {
			con.close();
			JOptionPane.showMessageDialog(null, "Não foi possível gravar. "+ex.getMessage());
		}
	}
	
	private void atualizar() throws SQLException {
		Connection con = null;
		ConexaoBanco objconexao = new ConexaoBanco();
		txtDescricao.setEditable(true);
		
		try {
			con = objconexao.conectar();
			
			if(con == null) {
				JOptionPane.showMessageDialog(null, "Conexao não realizada!");
			}
			else {
				Statement stmt = con.createStatement();
				String query="update db_pedido.produto set descricao = '"+txtDescricao.getText()+"' where id = '"+txtID.getText()+"'";
				stmt.executeUpdate(query);
				listarProduto();
				txtDescricao.setText(null);
			}	
		}
		catch(Exception ex) {
			con.close();
			JOptionPane.showMessageDialog(null, "Não foi possível gravar. "+ex.getMessage());
		}
	}
}
