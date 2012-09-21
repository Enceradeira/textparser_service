package uk.co.jennius.textparser;

public enum TypedDependencyOptions {
	// allows to reconstruct original text by concatenating the returned
	// dependencies and fillings
	PreserveSentenceStructure,
	// only returns the dependencies. Unimportant words, spaces, punctuation
	// etc... are removed
	OnlyDependencies
}
