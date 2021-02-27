#include "Atmosphere.h"

Layer* Atmosphere::transform(Oxigen x, char w) {
	Layer* newLayer = x.transform(w);
	if (newLayer != nullptr) {
		transformedLayers.push_back(newLayer);
	}
	return newLayer;
}
Layer* Atmosphere::transform(Ozone z, char w) {
	Layer* newLayer = z.transform(w);
	if (newLayer != nullptr) {
		transformedLayers.push_back(newLayer);
	}
	return newLayer;
}
Layer* Atmosphere::transform(CarbonDioxide s, char w) {
	Layer* newLayer = s.transform(w);
	if (newLayer != nullptr) {
		transformedLayers.push_back(newLayer);
	}
	return newLayer;
}
