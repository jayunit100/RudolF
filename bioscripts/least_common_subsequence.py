#!/usr/bin/env perl
#
# This is an implementation, in Perl, of the Longest Common Subsequence algorithm.
# That is, given two strings A and B, this program will find the longest sequence
# of letters that are common and ordered in A and B.
#
# There are only two reasons you are reading this:
#   - you don't care what the algorithm is but you need a piece of code to do it
#   - you're trying to understand the algorithm, and a piece of code might help
# In either case, you should either read an entire chapter of an algorithms textbook
# on the subject of dynamic programming, or you should consult a webpage that describes
# this particular algorithm.   It is important, for example, that we use arrays of size
# |A|+1 x |B|+1. 
#
# This code is provided AS-IS.
# You may use this code in any way you see fit, EXCEPT as the answer to a homework
# problem or as part of a term project in which you were expected to arrive at this
# code yourself.  
# 
# Copyright (C) 2005 Neil Jones.
#--------------------------------------------------------------------------------------

my ($a, $b) = @ARGV;
print LCS($a, $b), "\n";

sub LCS {
	my ($a, $b) = @_;
	my $S = [];   # An array of scores
	my $R = [];   # An array of backtracking arrows
	my $n = length($a);
	my $m = length($b);
	
	# We need to work in letters, not in strings.  This is a simple way
	# to turn a string of letters into an array of letters.
	my @a = split // => $a;
	my @b = split // => $b;

	# These are "constants" which indicate a direction in the backtracking array.
	my $NEITHER     = 0;
	my $UP          = 1;
	my $LEFT        = 2;
	my $UP_AND_LEFT = 3;
	
	# It is important to use <=, not <.  The next two for-loops are initialization
	for(my $ii = 0; $ii <= $n; ++$ii) { 
		$S->[$ii][0] = 0;
		$R->[$ii][0] = $UP;
	}
	
	for(my $jj = 0; $jj <= $m; ++$jj) { 
		$S->[0][$jj] = 0;
		$R->[0][$jj] = $LEFT;
	}

	# This is the main dynamic programming loop that computes the score and
	# backtracking arrays.
	for(my $ii = 1; $ii <= $n; ++$ii) {
		for(my $jj = 1; $jj <= $m; ++$jj) { 

			($S->[$ii][$jj], $R->[$ii][$jj]) = ($a[$ii-1] eq $b[$jj-1]) ?
			   ($S->[$ii-1][$jj-1] + 1, $UP_AND_LEFT) :
			   ($S->[$ii-1][$jj-1] + 0, $NEITHER);

			($S->[$ii][$jj], $R->[$ii][$jj]) = $S->[$ii-1][$jj] >= $S->[$ii][$jj] ?
			   ($S->[$ii-1][$jj], $UP) : 
			   ($S->[$ii][$jj], $R->[$ii][$jj]);

			($S->[$ii][$jj], $R->[$ii][$jj]) = $S->[$ii][$jj-1] >= $S->[$ii][$jj] ?
			   ($S->[$ii][$jj-1], $LEFT) : 
			   ($S->[$ii][$jj], $R->[$ii][$jj]);
		}
	}

	# The length of the longest substring is $S->[$n][$m]
	$ii = $n; 
	$jj = $m;
	my $lcs = '';

	# Trace the backtracking matrix.
	while( $ii > 0 || $jj > 0 ) {
		if( $R->[$ii][$jj] == $UP_AND_LEFT ) {
			$ii--;
			$jj--;
			$lcs = $a[$ii].$lcs;
		}

		elsif( $R->[$ii][$jj] == $UP ) {
			$ii--;
		}

		elsif( $R->[$ii][$jj] == $LEFT ) {
			$jj--;
		}

		else {
			die("Uninitialized arrow at ($ii, $jj): $S->[$ii][$jj] / $R->[$ii][$jj]\n");
		}
	}

	return $lcs;
}

