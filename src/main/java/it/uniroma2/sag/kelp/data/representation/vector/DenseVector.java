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

import it.uniroma2.sag.kelp.data.representation.Vector;

import org.ejml.alg.dense.mult.VectorVectorMult;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;
import org.ejml.ops.NormOps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Dense Feature Vector. 
 * <br>It uses <a href="https://code.google.com/p/efficient-java-matrix-library"> EJML </a> to guarantee a
 * very fast computation
 * 
 * @author Simone Filice
 */

@JsonTypeName("DV")
public class DenseVector implements Vector {
	private Logger logger = LoggerFactory.getLogger(DenseVector.class);
	private static final long serialVersionUID = 1150851329091800382L;
	private static final String SEPARATOR=" |,";//a space or a comma can separate feature values
	
	@JsonIgnore
	private DenseMatrix64F featuresValues;

	/**
	 * Empty constructor necessary for making <code>RepresentationFactory</code> support this implementation.
	 * 
	 * @param featureVector
	 *            is an array of feature values
	 */
	public DenseVector() {
		
	}
	
	@Override
	public void setDataFromText(String representationDescription) {
		String [] stringFeatures = representationDescription.split(SEPARATOR);
		float [] features = new float [stringFeatures.length];
		
		for(int i=0; i<stringFeatures.length; i++){
			features[i]= Float.parseFloat(stringFeatures[i]);
		}
		this.setFeatureValues(features);
		
	}
	
	/**
	 * Initializing constructor.
	 * 
	 * @param featureVector
	 *            is an array of feature values
	 */
	public DenseVector(float[] featureVector) {
		this.setFeatureValues(featureVector);
	}

	/**
	 * Initializing constructor.
	 * 
	 * @param featureVector
	 *            is an array of feature values in the EJML format
	 */
	public DenseVector(DenseMatrix64F featureVector) {
		this.featuresValues = featureVector;
	}

	/**
	 * Sets the feature values.
	 * 
	 * @param featureValues
	 *            is an array of feature values
	 */
	public void setFeatureValues(float[] featureValues) {
		this.featuresValues = new DenseMatrix64F(1, featureValues.length);
		for (int i = 0; i < featureValues.length; i++) {
			this.featuresValues.set(0, i, (double) featureValues[i]);
		}

	}

	/**
	 * Sets the feature values.
	 * 
	 * @param featureVector
	 *            is an array of feature values in the EJML format
	 */
	public void setFeatureValues(DenseMatrix64F featureVector) {
		this.featuresValues = featureVector;

	}

	/**
	 * Returns the feature values in the EJML format
	 * 
	 * @return the feature values
	 */
	@JsonIgnore
	public DenseMatrix64F getFeatureValues() {
		return featuresValues;
	}

	/**
	 * Returns the feature value of the <code>featureIndex</code>-th element
	 * 
	 * @param featureIndex
	 *            the index of the feature whose value must be returned
	 * @return the value of the feature
	 */
	public float getFeatureValue(int featureIndex) {
		return (float) featuresValues.get(0, featureIndex);
	}

	/**
	 * Returns the number of featuresValues
	 * 
	 * @return the number of featuresValues
	 */
	@JsonIgnore
	public int getNumberOfFeatures() {
		return this.featuresValues.numCols;
	}

	@Override
	public boolean equals(Object featureVector) {
		if (featureVector == null) {
			return false;
		}
		if (this == featureVector) {
			return true;
		}
		if (featureVector instanceof DenseVector) {
			DenseVector that = (DenseVector) featureVector;
			if (this.getNumberOfFeatures() == that.getNumberOfFeatures()) {
				for (int i = 0; i < this.getNumberOfFeatures(); i++) {
					if (this.getFeatureValue(i) != that.getFeatureValue(i)) {
						return false;
					}
				}
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		if (this.featuresValues.numCols == 0) {
			return "";
		}
		StringBuilder ret = new StringBuilder();
		ret.append(Float.toString(this.getFeatureValue(0)));
		for (int i = 1; i < this.featuresValues.numCols; i++) {
			ret = ret.append(" " + Float.toString(this.getFeatureValue(i)));
		}

		return ret.toString().trim();
	}

	@Override
	public void normalize() {
		double norm = NormOps.fastNormP2(this.featuresValues);

		CommonOps.divide(norm, this.featuresValues);
	}

	@Override
	public float innerProduct(Vector vector) {
		if(vector instanceof DenseVector){
			DenseVector dense = (DenseVector) vector;
			if (featuresValues == null)
				logger.debug("Features Values are null");
			return (float)VectorVectorMult.innerProd(featuresValues, dense.getFeatureValues());
		}
	
		throw new IllegalArgumentException("Expected a DenseVector to performe the innerProduct");
	}

	@Override
	public void scale(float coeff) {
		CommonOps.scale(coeff, this.featuresValues);
		
	}

	@Override
	public void add(Vector vector) {
		CommonOps.addEquals(this.featuresValues, ((DenseVector)vector).featuresValues);		
		
	}

	@Override
	public void add(float coeff, Vector vector) {
		
		CommonOps.addEquals(this.featuresValues, coeff, ((DenseVector)vector).featuresValues);
		
	}

	@Override
	public void add(float coeff, float vectorCoeff, Vector vector) {
		this.scale(coeff);
		this.add(vectorCoeff, vector);
		
	}

	@JsonIgnore
	@Override
	public Vector getZeroVector() {
		DenseMatrix64F dense = new DenseMatrix64F(this.featuresValues.numRows, this.featuresValues.numCols);
		DenseVector vector = new DenseVector(dense);		
		return vector;
	}

	@Override
	public String getTextFromData() {
		return this.toString();
	}

	@Override
	@JsonIgnore
	public float getSquaredNorm() {
		double norm = NormOps.fastNormP2(this.featuresValues);
		return (float)(norm*norm);
	}
}