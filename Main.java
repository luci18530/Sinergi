import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

abstract class Funcionario {
    String nome;
    String cargo;
    Calendar dataContratacao;
    double salarioBase;

    Funcionario(String nome, String cargo, Calendar dataContratacao, double salarioBase) {
        this.nome = nome;
        this.cargo = cargo;
        this.dataContratacao = dataContratacao;
        this.salarioBase = salarioBase;
    }

    abstract double calcularSalario(int mes, int ano);
    abstract double calcularBeneficio(int mes, int ano);
}

class Secretario extends Funcionario {
    static final double AUMENTO_ANUAL = 1000.0;
    static final double BENEFICIO = 0.20;

    Secretario(String nome, Calendar dataContratacao) {
        super(nome, "Secretário", dataContratacao, 7000.0);
    }

    @Override
    double calcularSalario(int mes, int ano) {
        int anosDeServico = ano - dataContratacao.get(Calendar.YEAR);
        return salarioBase + (AUMENTO_ANUAL * anosDeServico);
    }

    @Override
    double calcularBeneficio(int mes, int ano) {
        return calcularSalario(mes, ano) * BENEFICIO;
    }
}

class Vendedor extends Funcionario {
    static final double AUMENTO_ANUAL = 1800.0;
    static final double COMISSAO = 0.30;
    List<RegistroVendas> vendas;

    Vendedor(String nome, Calendar dataContratacao) {
        super(nome, "Vendedor", dataContratacao, 12000.0);
        this.vendas = new ArrayList<>();
    }

    void adicionarVenda(int mes, int ano, double valor) {
        vendas.add(new RegistroVendas(mes, ano, valor));
    }

    @Override
    double calcularSalario(int mes, int ano) {
        int anosDeServico = ano - dataContratacao.get(Calendar.YEAR);
        return salarioBase + (AUMENTO_ANUAL * anosDeServico);
    }

    @Override
    double calcularBeneficio(int mes, int ano) {
        for (RegistroVendas venda : vendas) {
            if (venda.getMes() == mes && venda.getAno() == ano) {
                return venda.getValor() * COMISSAO;
            }
        }
        return 0.0;
    }
}

class Gerente extends Funcionario {
    static final double AUMENTO_ANUAL = 3000.0;

    Gerente(String nome, Calendar dataContratacao) {
        super(nome, "Gerente", dataContratacao, 20000.0);
    }

    @Override
    double calcularSalario(int mes, int ano) {
        int anosDeServico = ano - dataContratacao.get(Calendar.YEAR);
        return salarioBase + (AUMENTO_ANUAL * anosDeServico);
    }

    @Override
    double calcularBeneficio(int mes, int ano) {
        return 0.0;
    }
}

class RegistroVendas {
    private int mes;
    private int ano;
    private double valor;

    RegistroVendas(int mes, int ano, double valor) {
        this.mes = mes;
        this.ano = ano;
        this.valor = valor;
    }

    public int getMes() {
        return mes;
    }

    public int getAno() {
        return ano;
    }

    public double getValor() {
        return valor;
    }
}

class Empresa {
    private List<Funcionario> funcionarios;

    Empresa() {
        this.funcionarios = new ArrayList<>();
    }

    void adicionarFuncionario(Funcionario funcionario) {
        funcionarios.add(funcionario);
    }

    double calcularTotalPago(int mes, int ano) {
        double total = 0.0;
        for (Funcionario funcionario : funcionarios) {
            total += funcionario.calcularSalario(mes, ano) + funcionario.calcularBeneficio(mes, ano);
        }
        return total;
    }

    double calcularTotalSalarios(int mes, int ano) {
        double total = 0.0;
        for (Funcionario funcionario : funcionarios) {
            total += funcionario.calcularSalario(mes, ano);
        }
        return total;
    }

    double calcularTotalBeneficios(int mes, int ano) {
        double total = 0.0;
        for (Funcionario funcionario : funcionarios) {
            total += funcionario.calcularBeneficio(mes, ano);
        }
        return total;
    }

    Funcionario obterFuncionarioComMaiorPagamento(int mes, int ano) {
        Funcionario maiorPagamentoFuncionario = null;
        double maiorPagamento = 0.0;

        for (Funcionario funcionario : funcionarios) {
            double pagamento = funcionario.calcularSalario(mes, ano) + funcionario.calcularBeneficio(mes, ano);
            if (pagamento > maiorPagamento) {
                maiorPagamento = pagamento;
                maiorPagamentoFuncionario = funcionario;
            }
        }
        return maiorPagamentoFuncionario;
    }

    Funcionario obterFuncionarioComMaiorBeneficio(int mes, int ano) {
        Funcionario maiorBeneficioFuncionario = null;
        double maiorBeneficio = 0.0;

        for (Funcionario funcionario : funcionarios) {
            double beneficio = funcionario.calcularBeneficio(mes, ano);
            if (beneficio > maiorBeneficio) {
                maiorBeneficio = beneficio;
                maiorBeneficioFuncionario = funcionario;
            }
        }
        return maiorBeneficioFuncionario;
    }

    Vendedor obterVendedorComMaiorVenda(int mes, int ano) {
        Vendedor maiorVendedor = null;
        double maiorVenda = 0.0;

        for (Funcionario funcionario : funcionarios) {
            if (funcionario instanceof Vendedor) {
                Vendedor vendedor = (Vendedor) funcionario;
                for (RegistroVendas venda : vendedor.vendas) {
                    if (venda.getMes() == mes && venda.getAno() == ano) {
                        if (venda.getValor() > maiorVenda) {
                            maiorVenda = venda.getValor();
                            maiorVendedor = vendedor;
                        }
                    }
                }
            }
        }
        return maiorVendedor;
    }
}

public class Main {
    public static void main(String[] args) {
        Empresa empresa = new Empresa();

        Calendar dataContratacao1 = Calendar.getInstance();
        dataContratacao1.set(2018, Calendar.JANUARY, 1);
        Secretario jorge = new Secretario("Jorge Carvalho", dataContratacao1);

        Calendar dataContratacao2 = Calendar.getInstance();
        dataContratacao2.set(2015, Calendar.DECEMBER, 1);
        Secretario maria = new Secretario("Maria Souza", dataContratacao2);

        Calendar dataContratacao3 = Calendar.getInstance();
        dataContratacao3.set(2021, Calendar.DECEMBER, 1);
        Vendedor ana = new Vendedor("Ana Silva", dataContratacao3);
        ana.adicionarVenda(12, 2021, 5200.0);
        ana.adicionarVenda(1, 2022, 4000.0);
        ana.adicionarVenda(2, 2022, 4200.0);
        ana.adicionarVenda(3, 2022, 5850.0);
        ana.adicionarVenda(4, 2022, 7000.0);

        Vendedor joao = new Vendedor("João Mendes", dataContratacao3);
        joao.adicionarVenda(12, 2021, 3400.0);
        joao.adicionarVenda(1, 2022, 7700.0);
        joao.adicionarVenda(2, 2022, 5000.0);
        joao.adicionarVenda(3, 2022, 5900.0);
        joao.adicionarVenda(4, 2022, 6500.0);

        Calendar dataContratacao4 = Calendar.getInstance();
        dataContratacao4.set(2017, Calendar.JULY, 1);
        Gerente juliana = new Gerente("Juliana Alves", dataContratacao4);

        Calendar dataContratacao5 = Calendar.getInstance();
        dataContratacao5.set(2014, Calendar.MARCH, 1);
        Gerente bento = new Gerente("Bento Albino", dataContratacao5);

        empresa.adicionarFuncionario(jorge);
        empresa.adicionarFuncionario(maria);
        empresa.adicionarFuncionario(ana);
        empresa.adicionarFuncionario(joao);
        empresa.adicionarFuncionario(juliana);
        empresa.adicionarFuncionario(bento);

        int mes = 4; // Abril
        int ano = 2022;

        System.out.println("Total pago em abril de 2022: R$ " + empresa.calcularTotalPago(mes, ano));
        System.out.println("Total pago em salários em abril de 2022: R$ " + empresa.calcularTotalSalarios(mes, ano));
        System.out.println("Total pago em benefícios em abril de 2022: R$ " + empresa.calcularTotalBeneficios(mes, ano));

        Funcionario funcionarioMaiorPagamento = empresa.obterFuncionarioComMaiorPagamento(mes, ano);
        System.out.println("Funcionário com maior pagamento em abril de 2022: " + funcionarioMaiorPagamento.nome);

        Funcionario funcionarioMaiorBeneficio = empresa.obterFuncionarioComMaiorBeneficio(mes, ano);
        System.out.println("Funcionário com maior benefício em abril de 2022: " + funcionarioMaiorBeneficio.nome);

        Vendedor vendedorMaiorVenda = empresa.obterVendedorComMaiorVenda(mes, ano);
        System.out.println("Vendedor com maior venda em abril de 2022: " + vendedorMaiorVenda.nome);

    }
}
