
package BioComputation;


public class Rule {

    int ConL = 5;
    String[] cond;
    String out;

 
    public Rule(int ConL) {
        this.ConL = ConL;
        this.cond = new String[ConL];
        this.out = "";
    }

    public Rule() {
        this.cond = new String[ConL];
        this.out = "";
    }
}
