package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class BeautyCategoryAnalyzer {
	public static final double MINIMUM_CATEGORY_PERCENTAGE = 20.0;

	private static final List<String> CATEGORY_KEYWORDS = List.of(
			"skin", "skincare", "face", "cream", "serum", "eye", "lip", "makeup", "beauty", "hydrat", "glow", "lash");

	private BeautyCategoryAnalyzer() {
	}

	public static AnalysisResult analyze(List<String> recommendationNames) {
		List<RecommendationClassification> classifications = new ArrayList<>();
		if (recommendationNames != null) {
			for (String recommendationName : recommendationNames) {
				if (recommendationName == null || recommendationName.isBlank()) {
					continue;
				}

				String normalizedName = normalize(recommendationName);
				List<String> matchingKeywords = CATEGORY_KEYWORDS.stream()
						.filter(normalizedName::contains)
						.toList();
				classifications.add(new RecommendationClassification(recommendationName, matchingKeywords));
			}
		}

		long matchingCount = classifications.stream().filter(RecommendationClassification::isBeautyCategory).count();
		double percentage = classifications.isEmpty() ? 0.0 : matchingCount * 100.0 / classifications.size();
		return new AnalysisResult(classifications, percentage);
	}

	private static String normalize(String productName) {
		return productName.toLowerCase(Locale.ROOT)
				.replaceAll("[^\\p{L}\\p{N}]+", " ")
				.replaceAll("\\s+", " ")
				.trim();
	}

	public record RecommendationClassification(String productName, List<String> matchingKeywords) {
		public boolean isBeautyCategory() {
			return !matchingKeywords.isEmpty();
		}
	}

	public record AnalysisResult(
			List<RecommendationClassification> classifications,
			double beautyCategoryPercentage) {

		public int totalRecommendations() {
			return classifications.size();
		}

		public long beautyCategoryCount() {
			return classifications.stream().filter(RecommendationClassification::isBeautyCategory).count();
		}

		public boolean meetsThreshold(double thresholdPercentage) {
			return totalRecommendations() > 0 && beautyCategoryPercentage >= thresholdPercentage;
		}

		public List<String> matchingRecommendations() {
			return classifications.stream()
					.filter(RecommendationClassification::isBeautyCategory)
					.map(RecommendationClassification::productName)
					.toList();
		}

		public List<String> nonMatchingRecommendations() {
			return classifications.stream()
					.filter(classification -> !classification.isBeautyCategory())
					.map(RecommendationClassification::productName)
					.toList();
		}

		public String diagnosticMessage(double thresholdPercentage) {
			return String.format(Locale.ROOT,
					"All recommendations: %s; matching recommendations: %s; non-matching recommendations: %s; "
							+ "beauty-category count: %d/%d; percentage: %.1f%% (required: %.1f%%)",
					classifications.stream().map(RecommendationClassification::productName).toList(),
					matchingRecommendations(),
					nonMatchingRecommendations(),
					beautyCategoryCount(),
					totalRecommendations(),
					beautyCategoryPercentage,
					thresholdPercentage);
		}
	}
}