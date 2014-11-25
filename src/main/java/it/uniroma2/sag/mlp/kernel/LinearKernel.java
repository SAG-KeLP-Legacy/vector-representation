package it.uniroma2.sag.mlp.kernel;

import com.fasterxml.jackson.annotation.JsonTypeName;

import it.uniroma2.sag.mlp.kernel.DirectKernel;
import it.uniroma2.sag.mlp.representation.Vector;

/**
 * Linear Kernel for <code>Vector</code>s <br>
 * It executes the dot product between two <code>Vector</code> representations
 * 
 * @author Simone Filice
 */

@JsonTypeName("linear")
public class LinearKernel extends DirectKernel<Vector> {

	public LinearKernel(String representationIdentifier) {
		super(representationIdentifier);

	}

	public LinearKernel() {

	}

	@Override
	protected float kernelComputation(Vector repA, Vector repB) {
		return repA.innerProduct(repB);
	}
}