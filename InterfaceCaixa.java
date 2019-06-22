import javafx.event.*;
import javafx.geometry.Insets;
import javafx.util.StringConverter;
import java.time.LocalDate;
import java.util.*;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.*;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.RadioButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.beans.value.ObservableValue;

public class InterfaceCaixa extends Pagina {

	public static final String titulo = "Caixa";
	public static final Image icone = new Image("img/menu-caixa.png");

	private OperadorEstoque operadorEstoque;
	private OperadorVenda operadorVenda;

	private TableView<Produto> tblListaProdutos;
	private TableView<ProdutoVenda> tblCaixa;
	private Venda venda;

	private TextField tfBusca;
	private Text tTotal;

	public InterfaceCaixa(Layout layout, OperadorVenda operadorVenda, OperadorEstoque operadorEstoque) {
		super(layout);
		super.alteraTitulo(titulo);
		super.selecionaBotao(layout.btnCaixa);

		this.operadorEstoque = operadorEstoque;
		this.operadorVenda = operadorVenda;

		// Instanciar nova venda
		novaVenda();

		// Lado esquerdo

		// Barra de opções

		// Campo de busca
		tfBusca = new TextField();
		tfBusca.setPromptText("ID ou Título de um Produto");
		
		// Botão de busca
		Button btnBuscar = new Button("Buscar");
		HBox.setHgrow(tfBusca, Priority.ALWAYS);

		// Barra de Busca
		HBox barraListaProdutos = new HBox();
		barraListaProdutos.getStyleClass().add("barraOpcoes");
		barraListaProdutos.getChildren().addAll(tfBusca, btnBuscar);

		// Botão Buscar
		btnBuscar.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				realizarBusca(tfBusca.getText());
			}
		});

		// Tabela
		tblListaProdutos = new TableView<Produto>();
		tblListaProdutos.setPlaceholder(new Label("É necessário inserir uma ID ou título."));

		// Coluna ID
		TableColumn<Produto, Integer> colunaId1 = new TableColumn<Produto, Integer>("ID");
		colunaId1.setCellValueFactory(new PropertyValueFactory<Produto, Integer>("id"));
		colunaId1.setPrefWidth(35);
		tblListaProdutos.getColumns().add(colunaId1);

		// Coluna Título
		TableColumn<Produto, String> colunaTitulo1 = new TableColumn<Produto, String>("Título");
		colunaTitulo1.setCellValueFactory(new PropertyValueFactory<Produto, String>("titulo"));
		colunaTitulo1.setPrefWidth(170);
		tblListaProdutos.getColumns().add(colunaTitulo1);

		// Coluna Preço
		TableColumn<Produto, Double> colunaPreco1 = new TableColumn<Produto, Double>("Preço");
		colunaPreco1.setCellValueFactory(new PropertyValueFactory<Produto, Double>("preco"));
		colunaPreco1.setPrefWidth(50);
		tblListaProdutos.getColumns().add(colunaPreco1);

		// Conteúdo Esquerdo
		BorderPane conteudoListaProdutos = new BorderPane();
		conteudoListaProdutos.setStyle("-fx-padding: 0 5 0 0");
		conteudoListaProdutos.setTop(barraListaProdutos);
		conteudoListaProdutos.setCenter(tblListaProdutos);

		// Ao selecionar uma célula da tabela de Produto
		tblListaProdutos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				Produto produtoSelecionado = tblListaProdutos.getSelectionModel().getSelectedItem();
				solicitaQuantidade(produtoSelecionado);
			}
		});

		// Lado direito

		// Botão para Finalizar Caixa
		Button btnFinalizarCaixa = new Button("Finalizar caixa");

		// Total
		tTotal = new Text("R$ 0,00");
		tTotal.getStyleClass().add("total");

		// Opções do Caixa
		BorderPane barraCaixa = new BorderPane();
		barraCaixa.getStyleClass().add("barraOpcoes");
		barraCaixa.setLeft(tTotal);
		barraCaixa.setRight(btnFinalizarCaixa);

		// Tabela
		tblCaixa = new TableView<ProdutoVenda>();
		tblCaixa.setPlaceholder(new Label("Aguardando a seleção de um produto."));

		// Coluna Qtd.
		TableColumn<ProdutoVenda, Integer> colunaQtd2 = new TableColumn<ProdutoVenda, Integer>("Qtd.");
		colunaQtd2.setCellValueFactory(new PropertyValueFactory<ProdutoVenda, Integer>("quantidade"));
		colunaQtd2.setPrefWidth(40);
		tblCaixa.getColumns().add(colunaQtd2);

		// Coluna ID
		TableColumn<ProdutoVenda, Integer> colunaId2 = new TableColumn<ProdutoVenda, Integer>("ID");
		colunaId2.setCellValueFactory(new PropertyValueFactory<ProdutoVenda, Integer>("id"));
		colunaId2.setPrefWidth(30);
		tblCaixa.getColumns().add(colunaId2);

		// Coluna Título
		TableColumn<ProdutoVenda, String> colunaTitulo2 = new TableColumn<ProdutoVenda, String>("Título");
		colunaTitulo2.setCellValueFactory(new PropertyValueFactory<ProdutoVenda, String>("titulo"));
		colunaTitulo2.setPrefWidth(150);
		tblCaixa.getColumns().add(colunaTitulo2);

		// Coluna Preço
		TableColumn<ProdutoVenda, Double> colunaPreco2 = new TableColumn<ProdutoVenda, Double>("Preço");
		colunaPreco2.setCellValueFactory(new PropertyValueFactory<ProdutoVenda, Double>("preco"));
		colunaId2.setPrefWidth(45);
		tblCaixa.getColumns().add(colunaPreco2);

		// Conteúdo Direito
		BorderPane conteudoCaixa = new BorderPane();
		conteudoCaixa.setStyle("-fx-padding: 0 0 0 5");
		conteudoCaixa.setTop(barraCaixa);
		conteudoCaixa.setCenter(tblCaixa);

		// Ao selecionar uma célula da tabela de ProdutoVenda
		tblCaixa.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				ProdutoVenda produtoSelecionado = tblCaixa.getSelectionModel().getSelectedItem();
				
			}
		});

		// Botão Finalizar Venda
		btnFinalizarCaixa.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				if(venda.vazia()) {
					Alerta.erro("Não foram inseridos produtos no caixa.");
				} else {
					solicitaPagamento();
				}
			}
		});

		// Divisão do conteúdo
		SplitPane splitPane = new SplitPane();
		splitPane.prefHeightProperty().bind(layout.conteudo.heightProperty());
		splitPane.getItems().addAll(conteudoListaProdutos, conteudoCaixa);
		super.layout.conteudo.getChildren().add(splitPane);
	}

	private void realizarBusca(String filtro) {
		tblListaProdutos.getItems().clear();
		for (Produto produto: operadorEstoque.lista) {
			String titulo = produto.getTitulo();
			String id = String.valueOf(produto.getId());
			if(titulo.indexOf(filtro) != -1 || id.indexOf(filtro) != -1) {
				tblListaProdutos.getItems().add(produto);
			}
		}
	}

	private void solicitaQuantidade(Produto produto) {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Quantidade");
		dialog.setHeaderText("Adicione a quantidade:");

		// Campo com a quantidade
		TextField tfQuantidade = new TextField("1");

		// Invoca foco no campo de texto
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				tfQuantidade.requestFocus();
			}
		});

		// Caixa de conteúdo
		HBox conteudo = new HBox();
		conteudo.setPadding(new Insets(10));
		conteudo.getChildren().add(tfQuantidade);
		dialog.getDialogPane().setContent(conteudo);

		// Botões
		ButtonType btnFinalizar = new ButtonType("Finalizar", ButtonData.OK_DONE);
		ButtonType btnCancelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(btnFinalizar, btnCancelar);
		
		Button getBtnFinalizar = (Button) dialog.getDialogPane().lookupButton(btnFinalizar);
		getBtnFinalizar.addEventFilter(ActionEvent.ACTION, event -> {
			ArrayList<String> erros = new ArrayList<String>();
			if(ValidaFormulario.nulo(tfQuantidade))
				erros.add("Quantidade vazia");
			if(!ValidaFormulario.inteiro(tfQuantidade))
				erros.add("O valor inserido não é um número inteiro");
			if(Conversor.StringParaInt(tfQuantidade.getText()) == 0)
				erros.add("O valor inserido deve ser diferente de 0.");
			if(erros.size() > 0) {
				Alerta.listaErros(erros);
				event.consume();
			}
		});

		Optional<ButtonType> resultado = dialog.showAndWait();
		if(resultado.get() == btnFinalizar) {

			// Quantidade de itens para o caixa
			int quantidade = Conversor.StringParaInt(tfQuantidade.getText());

			// Produto agora será um ProdutoVenda
			insereCaixa(produto, quantidade);

		}

		// Necessário para executar mudanças na interface após encerrar diálogo
		Platform.runLater(new Runnable() {
			@Override public void run() {
				limpaBusca();
				atualizaCaixa();
			}
		});
	}

	private void limpaBusca() {
		tblListaProdutos.getItems().clear();
		tfBusca.setText("");
	}

	private void atualizaCaixa() {
		tblCaixa.getItems().clear();
		for (ProdutoVenda produto: venda.lista) {
			tblCaixa.getItems().add(produto);
		}
	}

	private void insereCaixa(Produto produto, int quantidade) {

		// Verifica se já existe em caixa o produto
		ProdutoVenda produtoEmCaixa = null;
		for (ProdutoVenda produtoVenda: venda.lista) {
			if(produtoVenda.getId() == produto.getId()) 
				produtoEmCaixa = produtoVenda;
		}
		
		// Caso tenha em caixa, adiciona quantidade
		if(produtoEmCaixa != null)
			venda.inserirQuantidade(produtoEmCaixa, quantidade);

		// caso não tenha, o Produto será adicionado em Venda como ProdutoVenda
		else
			venda.inserirProduto(produto, quantidade);

		// Atualiza Texto com o Total da Venda
		atualizaTotal();
	}

	private void atualizaTotal() {
		tTotal.setText(Conversor.DoubleParaPreco(venda.getTotal()));
	}

	private void solicitaPagamento() {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Pagamento");
		dialog.setHeaderText("Informação de pagamento");

		VBox conteudo = new VBox();
		conteudo.setSpacing(5);

		ToggleGroup tgMeioPagamento = new ToggleGroup();

		int numMeiosPagamento = 0;
		for(MeioPagamento mp: operadorVenda.meiosPagamento) {
			RadioButton rb = new RadioButton(mp.getNome());
			rb.setUserData(mp.getNome());
			rb.setToggleGroup(tgMeioPagamento);
			conteudo.getChildren().add(rb);
			if(numMeiosPagamento == 0) rb.setSelected(true);
			numMeiosPagamento++;
		}

		// tgMeioPagamento.getSelectedToggle().getUserData().toString()

		dialog.getDialogPane().setContent(conteudo);

		// Botões
		ButtonType btnFinalizar = new ButtonType("Finalizar", ButtonData.OK_DONE);
		ButtonType btnCancelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(btnFinalizar, btnCancelar);

		Optional<ButtonType> resultado = dialog.showAndWait();
		if(resultado.get() == btnFinalizar) {
			String mp = tgMeioPagamento.getSelectedToggle().getUserData().toString();
			operadorVenda.finalizarVenda(venda, mp);
			novaVenda();
		}
	}

	private void novaVenda() {
		venda = new Venda();
		Platform.runLater(new Runnable() {
			@Override public void run() {
				limpaBusca();
				atualizaCaixa();
				atualizaTotal();
			}
		});
	}
}