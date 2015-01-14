#!/bin/sh

rm -fr broker01/data
rm -f broker01/lock

rm -fr broker02/data
rm -f broker02/lock

rm -fr broker03/data
rm -f broker03/lock

rm -fr shared-broker-data/kahadb
