package misc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import dc.ga.HelperClass;
import dc.ga.PreProcess;
import dc.ga.DCCurve.Event;
import dc.ga.DCCurve.Type;
import dc.io.FReader;
import dc.io.FReader.FileMember2;

public class DCCurveClassificationOlsen extends DCCurveRegression {

	public DCCurveClassificationOlsen() {
		super();
		meanRatio = new double[1];
		meanRatio[0] = 0.0;
		
		meanMagnitudeRatio =  new double[1];
		meanMagnitudeRatio[0] = 0.0;
	}

	public void build(Double[] values, double delta, String GPTreeFileName, Event[] events, 
			Event[] trainingOutput,PreProcess preprocess) {
		String thresholdStr = String.format("%.8f", delta);
		thresholdString = thresholdStr;
		
		meanRatio[0] = 2.0;
		meanMagnitudeRatio[0] = 1.0;
		if ( events == null || events.length <1)
			return;
		
		trainingEvents =  Arrays.copyOf(events, events.length) ;
		this.trainingOutputEvents =  Arrays.copyOf(trainingOutput, trainingOutput.length);
	}

	public void testbuild(int lastTrainingPricePosition, Double[] values, double delta, Event[] testEvents,
			PreProcess preprocess) {
		lastTrainingPrice = lastTrainingPricePosition;
		if (testEvents == null || testEvents.length < 1)
			return;

		testingEvents = Arrays.copyOf(testEvents, testEvents.length);

		predictionWithClassifier = new double[testEvents.length];
		for (int outputIndex = 0; outputIndex < testEvents.length - 1; outputIndex++) {

			double eval = 0.0;

			String classificationStr = "no";
			if (preprocess != null)
				classificationStr = preprocess.classifyTestInstance(outputIndex);

			if ((classificationStr.compareToIgnoreCase("no") == 0)) {
				;// System.out.println("no");
			} else {
				eval = testEvents[outputIndex].length() * 2;
			}
			predictionWithClassifier[outputIndex] = eval;
		}

	}

	private String calculateRMSE_Olsen(Event[] trendEvent, double delta, double[] runPrediction) {

		double rmse = 0.0;
		for (int eventCount = 0; eventCount < trendEvent.length; eventCount++) {
			int os = 0;

			if (trendEvent.length != runPrediction.length) {
				System.out.println("Event and prediction not equal");
				System.exit(0);
			}

			if (trendEvent[eventCount].overshoot != null) {
				os = trendEvent[eventCount].overshoot.length();
				// numberOfTestOvershoot = numberOfTestOvershoot + 1;
			}

			// numberOfTestDC = trendEvent.length;

			double prediction = runPrediction[eventCount];

			// System.out.println("DC:" + trendEvent[eventCount].length() + "
			// OS:" + os + " prediction:" + prediction);
			rmse = rmse + ((os - prediction) * (os - prediction));

			if (rmse == Double.MAX_VALUE || rmse == Double.NEGATIVE_INFINITY || rmse == Double.NEGATIVE_INFINITY
					|| rmse == Double.NaN || Double.isNaN(rmse) || Double.isInfinite(rmse)
					|| rmse == Double.POSITIVE_INFINITY) {
				System.out.println("Invalid RMSE: " + rmse + ". discarding ");
				// predictionRmseClassifier= 10.0;
				predictionRmse = Double.MAX_VALUE;
				return Double.toString(predictionRmse);
			}
		}

		predictionRmse = Math.sqrt(rmse / (trendEvent.length - 1));
		BigDecimal bd = null;
		BigDecimal bd2 = null;
		try {
			bd = new BigDecimal(predictionRmse);
			bd2 = new BigDecimal(Double.toString(predictionRmse));
			if (predictionRmse >= Double.MAX_VALUE)
				return Double.toString(10.0);
		} catch (NumberFormatException e) {
			System.out.println("Invalid predictionRmseClassifier: " + predictionRmse + " discarding ");
			predictionRmse = 10.0;
		}
		// System.out.println(predictionRmse);
		return Double.toString(predictionRmse);

	}

	////

	public String reportTestOlsen(Double[] values, double delta, String GPTreeFileName) {
		return calculateRMSE_Olsen(testingEvents, delta, predictionWithClassifier);

	}

	@Override
	String report(Double[] values, double delta, String GPTreeFileName) {
		return calculateRMSE_Olsen(testingEvents, delta, predictionWithClassifier);
	}



	@Override
	public String getDCCurveName() {

		return "DCCurveOlsen";
	}

	public void estimateTrainingUsingOutputData(PreProcess preprocess) {
		trainingUsingOutputData = new double[trainingOutputEvents.length];
		
		
		for (int outputIndex = 0; outputIndex < trainingOutputEvents.length; outputIndex++) {
			
			double eval=0.0;
			String classificationStr = "no";

			if (preprocess != null)
				classificationStr = preprocess.classifyTrainingInstance(outputIndex);

			if ((classificationStr.compareToIgnoreCase("no") == 0)) {
				;// System.out.println("no");
			 
			} else {
					eval = trainingOutputEvents[outputIndex].length() * 2;
			}

			trainingUsingOutputData[outputIndex] = eval;
		}
	}
	

	@Override
	void estimateTraining(PreProcess preprocess) {
		;
	}

	@Override
	public String getActualTrend() {
		return actualTrendString;
	}

	@Override
	public String getPredictedTrend() {
		return predictedTrendString;
	}
	
	@Override
	protected int getMaxTransactionSize() {
		// TODO Auto-generated method stub
		return positionArrayBase.size();
	}
	
	@Override
	protected double getTransanction(int i) {
		if ( i >= positionArrayBase.size())
			return 0.0;
		
		return positionArrayBase.get(i);
	}
	
	public  double calculateSD(){
		return calculateBaseSD(positionArrayBase);
	}
	
	public  double getMaxValue(){
		  
		  return getMaxValue(positionArrayBase);
		}
	public   double getMinValue(){
	 
	  return   getMinValue(positionArrayBase);
	}

	@Override
	public <E> void assignPerfectForesightRegressionModel(E[] inputArray) {
		meanRatio[0] = ((Double) inputArray[0]).doubleValue();
	}

}
