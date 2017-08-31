package BioInformics;

/*
 * Following is the naive BWT
 * Idea: 
 * 1. Concatenate two strings of texts then start from 0 up to length of text to get rotation of
 * strings with length equivalent to that of text  
 * 2. Sort the resulted rotation arrays based on lexicographical order
 * 3. Find the last one character of each sorted string array and concatenate them into a new string
 * 4. return that string as result
 * */

public class BWTMethod {
	//Here is the forward BWT
	public String ForwardBWT(String text){
		final int SIZE  = text.length();
		String result = "";
		String text_full  = text.concat(text);
		String[] text_array = new String[SIZE];
		for(int i = 0 ; i < text.length(); i ++){
			text_array[i] = text_full.substring(i, i+SIZE);
		}
		String[] text_sorted= sortStrings(text_array,SIZE);//sort the string;
		result = lastCharacter(text_sorted,SIZE);// find the last character of sorted string;
		return result;
	}
		public String[] sortStrings(String a[], int size){
			for(int i = 0; i < size - 1; i++){
				for(int j = 0; j < size-i-1; j++){
					if(a[j].compareTo(a[j+1])>0){
						String t = a[j];
						a[j] = a[j+1];
						a[j+1] = t;
					}
				}
			}
			return a;
		}
		public String lastCharacter(String[] b, int size){
			String result = "";
			for(int i = 0; i <size; i++){
				result = result + b[i].charAt(size-1);
			}
			return result;
		}
	//This is reverse BWT
		public static String ReverseBWT(String text){
			String result = "";//assume our input only contains alphabet letters excluding all other symbols
			final int SIZE = text.length(); //length of input text
			char[] resultChar = new char[SIZE];//char array of result
			char[] textChar = new char[SIZE];//char array of text
			char[] countFirstColumn = new char[SIZE];//first column 
			int[] indicatorF = new int[SIZE];//indicator for the first column
			int[] indicatorL = new int[SIZE];//indicator for the last column
			int pivot = 0;//where $ lies in the last column
			int n = 26;//how many alphabets used
			text = text.toLowerCase(); // assume that letter case does not matter
			for(int j = 0; j < SIZE; j++){
				textChar[j] = text.charAt(j);
				if(textChar[j] == '$'){
					indicatorL[j] = 0;
					pivot = j;
				}
			}
			// assume '$' is attached at the end of 
			//initial string as an indicator of ending character
			//length includes '$'
			int[] category = dicreteCat(SIZE,textChar,n);//array for storing cumulative accounts for each character
			int[] statCategoryF = dicreteCat(SIZE,textChar,n);
			int[] statCategoryL = dicreteCat(SIZE,textChar,n);
			int[] category_cum = cumuCat(n,category);
			for(int i = 0; i < SIZE; i++){
				if(i == 0){
					countFirstColumn[i] = '$';
					indicatorF[i] = 0;
				}else{
					for(int j = 0; j < n; j++){
						if(category_cum[j] >= i){
							countFirstColumn[i] = (char)('a' + j);
							break;
						}
					}
				}
			}
			
			for(int i = 1;i < SIZE;i++){
				int index = countFirstColumn[i] - 'a';
				indicatorF[i] = category [index] - statCategoryF[index];
				statCategoryF[index] --;
			}//associate first column with an array of integer functioning as indicators
			
			for(int j = 0; j < SIZE;j++){
				if(j == pivot){
					indicatorL[j] = 0;
				}else{
					int index = textChar[j] - 'a';
					indicatorL[j] = category[index] - statCategoryL[index];
					statCategoryL[index]--;
				}
				
			}//associate last column with an array of integer functioning as indicators
			resultChar = computeInverse(countFirstColumn, textChar, SIZE, indicatorF, indicatorL); //this needs revision
			for(int k = 0; k < SIZE; k++){
				result = result + resultChar[k]; 
			}
			return result;
		}
		
		
		//this gives accumulative count for occurrences of chars in array category
		public static int[] dicreteCat(int SIZE, char[] textChar,int n){
			int[] category = new int[n];
			for(int j = 0; j < n; j++){
				category[j] = 0;
			}
			for(int i = 0; i < SIZE; i++){
				if(textChar[i] != '$'){
					int index = textChar[i] - 'a';
					category[index]  = category[index] + 1;
				}
			}
			return category;
		}
		
		public static int[] cumuCat(int n, int[] category){
			int[] category_cum = new int[n];
			
			for(int j = 0;j < n; j++){
				category_cum[j] = category[j];
			}
			for(int j = 1;j < n; j++){
				category_cum[j] = category_cum[j] + category_cum[j-1];
			}
			return category_cum;
		}
		

		
		
		
		public static char[] computeInverse(char[] countFirstColumn, char[] textChar, int size, int[] indicatorF, int[] indicatorL){
			char[] resultChar = new char[size]; 
			int l_last = 0;
			resultChar[size-1] = '$';
			char lastChar = textChar[0];
			for(int p = 2; p <= size; p++){
				resultChar[size-p] = lastChar;
				for(int l = 1 ; l < size;l++){
					if(countFirstColumn[l] == lastChar && indicatorF[l] == indicatorL[l_last]){
						countFirstColumn[l] = ' ';
						lastChar = textChar[l];
						l_last = l;
						break;
					}
				}
				
			}
			return resultChar;
		}
}
