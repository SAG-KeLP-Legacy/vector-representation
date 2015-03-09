vector-representation
=========

[**KeLP**][kelp-site] is the Kernel-based Learning Platform developed in the [Semantic Analytics Group][sag-site] of
the [University of Roma Tor Vergata][uniroma2-site].

This is the vector-representation module of KeLP. It contains the representation such as DenseVector or SparseVector. These representations can be exploited both in linear and kernel-based algorithms. 

The _DenseVector_ representation should be adopted in modeling dense feature vectors in a small feature space, like an embedding. It relies on [EJML][ejml-site] for an efficient implementation. 

The _SparseVector_ representation is the best option for modeling sparse feature vector from high dimensional feature spaces, like a Bag-of-Words feature space. It relies on a hashmap implementation based on [TROVE][trove-site], in order to guarantee and efficient solution both from memory usage and computational perspectives. 

[sag-site]: http://sag.art.uniroma2.it "SAG site"
[uniroma2-site]: http://www.uniroma2.it "University of Roma Tor Vergata"
[ejml-site]: https://code.google.com/p/efficient-java-matrix-library/ "EJML site"
[trove-site]: http://trove.starlight-systems.com/news "TROVE site"
[kelp-site]: http://sag.art.uniroma2.it/demo-software/kelp/