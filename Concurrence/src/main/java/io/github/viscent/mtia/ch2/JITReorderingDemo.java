package io.github.viscent.mtia.ch2;

public class JITReorderingDemo {
	private int externalData = 1;
    private Helper helper;
    
    
	public static void main(String[] args) throws InstantiationException, 
				IllegalAccessException{
		
		

	}
	
	//@Actor
    public void createHelper() {
        helper = new Helper(externalData);
    }
	static class Helper {
		int payloadA;
        int payloadB;
        int payloadC;
        int payloadD;
        
        
        public Helper(int externalData) {
            this.payloadA = externalData;
            this.payloadB = externalData;
            this.payloadC = externalData;
            this.payloadD = externalData;
        }
        
        @Override
        public String toString() {
            return "Helper [" + payloadA + ", " + payloadB + ", " + payloadC + ", "
                    + payloadD + "]";
        }
	}
	
}
