package process;

import java.util.Random;


public class Rng {

    Random r = new Random();
    double plambda = 5.65; //poisson lambda
    double ulambda = 2.0; //Uniform lambda
    
    //poisson random number generator distribution
    public double getPoisson(){
        double x = Math.exp(-plambda);
        int y = 0;
        double t = 1.0;
        while(t > x){
            y++;
            t = t * r.nextDouble();
        }
        return y-1;
    }
    //uniform random number generator distribution
    public double getUniform(){
        double t = r.nextDouble();
        return ulambda*t;
    }
    //Poisson setters/getters
    public void setPlambda(double plambda){
        this.plambda = plambda;
    }
    public double getPlambda(){
        return plambda;
    }
    //Uniform setters/getters
    public void setUlambda(double ulambda){ this.ulambda = ulambda; }
    public double getUlambda(){ return ulambda; }
    }
