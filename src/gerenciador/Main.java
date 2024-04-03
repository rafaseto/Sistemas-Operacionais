package gerenciador;

public class Main {
    public static void main(String[] args) {
        //Gerenciador configurado para uma memória de 62KB e dividida em unidades de alocação de 2KB
        NextFit gerenciador = new NextFit(2, 62);

        //cria um processo que precisa de um segmento de memória com um comprimento maior ou igual a 10 para ser executado
        Processo d = new Processo(10);

        Processo e = new Processo(3);
        Processo f = new Processo(1);

        gerenciador.exibeListaEncadeada();

        gerenciador.alocaProcesso(d);

        gerenciador.exibeListaEncadeada();

        gerenciador.alocaProcesso(e);

        gerenciador.exibeListaEncadeada();

        gerenciador.alocaProcesso(f);

        gerenciador.exibeListaEncadeada();

        //Processo finalizado
        d.setOnExecution(false);

        gerenciador.exibeListaEncadeada();

        gerenciador.alocaProcesso(f);

        gerenciador.exibeListaEncadeada();
    }
}
