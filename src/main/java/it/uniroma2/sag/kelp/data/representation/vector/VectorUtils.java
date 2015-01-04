/*
 * Copyright 2014 Simone Filice and Giuseppe Castellucci and Danilo Croce and Roberto Basili
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.uniroma2.sag.kelp.data.representation.vector;

import it.uniroma2.sag.kelp.data.dataset.Dataset;
import it.uniroma2.sag.kelp.data.example.Example;
import it.uniroma2.sag.kelp.data.example.ExamplePair;
import it.uniroma2.sag.kelp.data.example.SimpleExample;
import it.uniroma2.sag.kelp.data.representation.Vector;

import java.util.List;

/**
 * A class containing some methods useful for dealing with vectors
 * 
 * @author Simone Filice
 */
public class VectorUtils {

	/**
	 * Returns a SparseVector corresponding to the concatenation of the vectors in <code>example</code> identified with <code>representationsToBeMerged</code>
	 * Each vector is scaled with respect to the corresponding scaling factor in <code>weights</code> 
	 * 
	 * @param example the example whose vectors must be concatenated
	 * @param representationsToBeMerged the identifiers of the vectors to be concatenated
	 * @param weights the scaling factors of the vectors to be concatenated
	 * @return a SparseVector corresponding to the concatenation of the vectors
	 */
	public static SparseVector mergeVectors(SimpleExample example, List<String> representationsToBeMerged, List<Float> weights){
		SparseVector vector = new SparseVector();
		for(int i=0; i<representationsToBeMerged.size(); i++){
			String representation = representationsToBeMerged.get(i);
			Vector vectorToBeAdded = (Vector) example.getRepresentation(representation);
			vector.merge(vectorToBeAdded, weights.get(i), representation);
		}
		
		return vector;
	}
	
	/**
	 * Add a new representation identified with <code>combinationName<code> corresponding to the concatenation of the vectors in <code>example</code> identified with <code>representationsToBeMerged</code>
	 * Each vector is scaled with respect to the corresponding scaling factor in <code>weights</code> 
	 * 
	 * @param example the example whose vectors must be concatenated
	 * @param representationsToBeMerged the identifiers of the vectors to be concatenated
	 * @param weights the scaling factors of the vectors to be concatenated
	 * @param combinationName the name of the new representation to be added
	 */
	public static void mergeVectors(Example example, List<String> representationsToBeMerged, List<Float> weights, String combinationName){
		if(example instanceof SimpleExample){
			SimpleExample ex = (SimpleExample)example;
			SparseVector combination = mergeVectors(ex, representationsToBeMerged, weights);
			ex.addRepresentation(combinationName, combination);
		}else if(example instanceof ExamplePair){
			ExamplePair ex = (ExamplePair)example;
			Example leftEx = ex.getLeftExample();
			Example rightEx = ex.getRightExample();
			mergeVectors(leftEx, representationsToBeMerged, weights, combinationName);
			mergeVectors(rightEx, representationsToBeMerged, weights, combinationName);
		}else{
			throw new IllegalArgumentException("Unsupported Example type: " + example.getClass().getSimpleName());
		}
		
	}
	
	/**
	 * Add to each example in <code>dataset</code> a new representation identified with <code>combinationName<code> corresponding to the concatenation of the vectors in <code>example</code> identified with <code>representationsToBeMerged</code>
	 * Each vector is scaled with respect to the corresponding scaling factor in <code>weights</code> 
	 * 
	 * @param dataset the dataset containing the examples whose vectors must be concatenated
	 * @param representationsToBeMerged the identifiers of the vectors to be concatenated
	 * @param weights the scaling factors of the vectors to be concatenated
	 * @param combinationName the name of the new representation to be added
	 */
	public static void mergeVectors(Dataset dataset, List<String> representationsToBeMerged, List<Float> weights, String combinationName){
		for(Example example : dataset.getExamples()){
			mergeVectors(example, representationsToBeMerged, weights, combinationName);
		}
	}
}
