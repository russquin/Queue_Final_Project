package process;

import java.util.Random;


public class Rng {

    Random r = new Random();
    double lambda = 5.7;


    public double getPoisson(){
        double x = Math.exp(-lambda);
        int y = 0;
        double t = 1.0;
        while(t > x){
            y++;
            t = t * r.nextDouble();
        }
        return y-1;
    }

    public void setLambda(double lambda){
        this.lambda = lambda;
    }

    public double getLambda(){
        return lambda;
    }
}