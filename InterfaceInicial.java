import javafx.scene.image.*;

public class InterfaceInicial extends Pagina {

	public static final String titulo = "Inicial";
	public static final Image icone = new Image("img/menu-inicial.png");

	public InterfaceInicial(Layout layout) {
		super(layout);
		super.alteraTitulo(titulo);
		super.selecionaBotao(layout.btnInicial);
	}
}