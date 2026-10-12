package utils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public final class RecommendationAnalyzer {
	public static final double MINIMUM_RELEVANCE_PERCENTAGE = 30.0;

	private static final List<String> BEAUTY_PRODUCT_KEYWORDS = List.of(
			"mascara", "moisturizer", "moisturiser", "serum", "vitamin c", "cleanser", "toner",
			"sunscreen", "sun screen", "foundation", "concealer", "blush", "bronzer", "highlighter",
			"lipstick", "lip gloss", "lip liner", "lip balm", "lip", "eyes", "eyeshadow", "eye shadow",
			"eyeliner", "eye liner", "primer", "powder", "skincare", "skin care", "makeup", "make up",
			"setting spray", "brow gel", "brow pencil", "face mask", "eye cream", "night cream",
			"day cream", "face oil", "facial oil", "exfoliant", "exfoliator", "micellar water",
			"face wash", "body wash", "body lotion", "hand cream", "shampoo", "conditioner", "hair mask",
			"hair oil", "dry shampoo", "fragrance", "perfume", "deodorant", "body scrub", "nail polish",
			"cuticle oil", "lash serum", "brow", "lashes", "bath", "skin tint", "tinted moisturizer",
			"cc cream", "bb cream", "color corrector", "lip treatment", "face mist", "essence", "ampoule"
	);

	private static final Set<String> NON_PRODUCT_WORDS = Set.of(
			"a", "an", "and", "best", "by", "for", "from", "in", "new", "of", "on", "the", "to",
			"with", "your", "our", "you", "ultra", "pro", "intense", "daily", "hydrating", "volume",
			"volumizing", "lengthening", "waterproof", "brightening", "natural", "smooth", "lightweight"
	);

	private RecommendationAnalyzer() {
	}

	public static AnalysisResult analyze(String viewedProductName, List<String> recommendationNames) {
		List<String> sourceNames = viewedProductName == null ? List.of() : List.of(viewedProductName);
		return analyze(sourceNames, recommendationNames);
	}

	public static AnalysisResult analyze(List<String> sourceProductNames, List<String> recommendationNames) {
		List<String> validSourceNames = sourceProductNames == null
				? List.of()
				: sourceProductNames.stream()
						.filter(name -> name != null && !name.isBlank())
						.toList();
		Set<String> combinedKeywords = new LinkedHashSet<>();
		for (String sourceProductName : validSourceNames) {
			combinedKeywords.addAll(findProductKeywords(normalize(sourceProductName)));
		}
		List<String> sourceKeywords = List.copyOf(combinedKeywords);
		List<RecommendationMatch> matches = new ArrayList<>();

		for (String recommendationName : recommendationNames) {
			if (recommendationName == null || recommendationName.isBlank()) {
				continue;
			}

			String normalizedRecommendationName = normalize(recommendationName);
			List<String> sharedKeywords = sourceKeywords.stream()
					.filter(keyword -> containsKeyword(normalizedRecommendationName, keyword))
					.toList();
			matches.add(new RecommendationMatch(recommendationName, sharedKeywords));
		}

		long relevantCount = matches.stream().filter(RecommendationMatch::isRelevant).count();
		double relevancePercentage = matches.isEmpty() ? 0.0 : relevantCount * 100.0 / matches.size();
		return new AnalysisResult(
				String.join(", ", validSourceNames), sourceKeywords, matches, relevancePercentage, validSourceNames);
	}

	private static List<String> findProductKeywords(String normalizedName) {
		Set<String> keywords = new LinkedHashSet<>();
		for (String keyword : BEAUTY_PRODUCT_KEYWORDS) {
			String normalizedKeyword = normalize(keyword);
			if (containsKeyword(normalizedName, normalizedKeyword)
					&& normalizedKeyword.split(" ").length == 1
					&& NON_PRODUCT_WORDS.contains(normalizedKeyword)) {
				continue;
			}
			if (containsKeyword(normalizedName, normalizedKeyword)) {
				keywords.add(normalizedKeyword);
			}
		}
		return List.copyOf(keywords);
	}

	private static boolean containsKeyword(String normalizedName, String normalizedKeyword) {
		return (" " + normalizedName + " ").contains(" " + normalizedKeyword + " ");
	}

	private static String normalize(String productName) {
		if (productName == null) {
			return "";
		}
		return productName.toLowerCase(Locale.ROOT)
				.replaceAll("[^\\p{L}\\p{N}]+", " ")
				.replaceAll("\\s+", " ")
				.trim();
	}

	public record RecommendationMatch(String productName, List<String> sharedKeywords) {
		public boolean isRelevant() {
			return !sharedKeywords.isEmpty();
		}
	}

	public record AnalysisResult(
			String viewedProductName,
			List<String> viewedProductKeywords,
			List<RecommendationMatch> recommendations,
			double relevancePercentage,
			List<String> sourceProductNames) {

		public AnalysisResult(
				String viewedProductName,
				List<String> viewedProductKeywords,
				List<RecommendationMatch> recommendations,
				double relevancePercentage) {
			this(viewedProductName, viewedProductKeywords, recommendations, relevancePercentage,
					viewedProductName == null ? List.of() : List.of(viewedProductName));
		}

		public int totalRecommendations() {
			return recommendations.size();
		}

		public long relevantRecommendationCount() {
			return recommendations.stream().filter(RecommendationMatch::isRelevant).count();
		}

		public boolean meetsThreshold(double thresholdPercentage) {
			return totalRecommendations() > 0 && relevancePercentage >= thresholdPercentage;
		}

		public String diagnosticMessage(double thresholdPercentage) {
			List<String> relevant = recommendations.stream()
					.filter(RecommendationMatch::isRelevant)
					.map(match -> match.productName() + " (shared keywords: " + match.sharedKeywords() + ")")
					.toList();
			List<String> notRelevant = recommendations.stream()
					.filter(match -> !match.isRelevant())
					.map(RecommendationMatch::productName)
					.toList();
			return String.format(Locale.ROOT,
					"Source products: %s; combined keywords: %s; total recommendations: %d; "
							+ "relevant recommendations: %d; relevance: %.1f%% (required: %.1f%%); "
							+ "relevant: %s; non-relevant: %s%s",
					sourceProductNames,
					viewedProductKeywords,
					totalRecommendations(),
					relevantRecommendationCount(),
					relevancePercentage,
					thresholdPercentage,
					relevant,
					notRelevant,
					recommendations.isEmpty() ? "; no recommendation products were found" : "");
		}
	}
}