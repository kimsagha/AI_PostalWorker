package com.aim.project.pwp.runners;

import java.util.Random;

import AbstractClasses.HyperHeuristic;

/**
 * Runs a test framework to compare greedy hh with simple random hh and then prints the results
 */
public class TestFramework_SRvsGREEDY extends TestFramework_Runner {
		

	
	public static void main(String [] args) {
		
		System.out.print("Running Kim's Test Framework!\n\n");
		long timeLimit = 60000;
		TestFramework_Runner runner = new TestFramework_SRvsGREEDY();
		SR_IE_VisualRunner sr = new SR_IE_VisualRunner();
		GREEDY_IE_VisualRunner greedy = new GREEDY_IE_VisualRunner();
		Random random = new Random(10231230L);
		int TOTAL_RUNS = 11;
		long[] seeds = new long[TOTAL_RUNS];
		
		double valueSR = 0;
		double valueGreedy = 0;
		double scoreSR = 0;
		double scoreGreedy = 0;
		double averageSR = 0;
		double averageGreedy = 0;
		int[] instances = new int[3];
		instances[0] = 2;
		instances[1] = 3;
		instances[2] = 4;
		
		for(int j = 0; j < 3; j++)	{
			System.out.print("Instance: " + j + "\n\n");
			for(int i = 0; i < TOTAL_RUNS; i++) {
				System.out.print("Trial: " + i + "\n");
				seeds[i] = random.nextLong();
				
				System.out.print("Running SR: ");
				HyperHeuristic srHH = sr.getHyperHeuristic(seeds[i]);
				srHH.setTimeLimit(timeLimit);
				valueSR = runner.run(srHH, seeds[i], instances[j]);
				System.out.print(valueSR + "\n");
				
				System.out.print("Running Greedy: ");
				HyperHeuristic greedyHH = greedy.getHyperHeuristic(seeds[i]);
				greedyHH.setTimeLimit(timeLimit);
				valueGreedy = runner.run(greedyHH, seeds[i], instances[j]);
				System.out.print(valueGreedy + "\n");
				
				if (valueGreedy < valueSR) {
					scoreSR++;
					System.out.print("Best: Greedy\n\n");
				}
				if (valueSR < valueGreedy) {
					scoreGreedy++;
					System.out.print("Best: SR\n\n");
				}
				else {
					scoreSR++;
					scoreGreedy++;
				}
			}
		}
		averageSR = scoreSR/(TOTAL_RUNS*3);
		averageGreedy = scoreGreedy/(TOTAL_RUNS*3);
		
		System.out.print("\nSR-score: " + scoreSR + " Greedy-score: " + scoreGreedy);
		System.out.printf("\nSR-average: %.2f", averageSR);
		System.out.printf(" Greedy-average: %.2f", averageGreedy);
		
		System.out.print("\n\nTestFramework_SRvsGREEDY successful\n");
		
	}

}
