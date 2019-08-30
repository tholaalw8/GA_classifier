
package BioComputation;


public class Data {
    int Vars; 
    int[] variables;
    int type;
    
    public Data(int Vars){
        this.Vars = Vars;
        this.variables = new int[Vars];
    }
    
    @Override
    public  String toString(){
        String s = "";
        for(int v:variables){
            s = s+v;
        }
      return  s+type;
      
      
}
}
