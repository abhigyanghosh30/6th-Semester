# UD to AnnCorra 

## Statistical Converter

### Files

#### Data Files
- `data.txt` - contains the data for testing in raw
- `modi.txt` - AnnCorra annotated corpus for training the parser on AnnCorra 
- `abhigyan.txt` -  AnnCorra annotated corpus for testing the conversion

#### Source Files
- `jsonify_ac.py` - converts AnnCorra annotated files to _json_ format
- `jsonify_ud.py` - converts UD annotated files to _json_ format
- `ud_depparse.py` - Parsing of data files into UD. **Not required if UD annotated files already available**
- `table.json` - Contains the mapping of UD to AnnCorra in _json_ format
- `train_ud_ac.py` - Main script to convert UD to AnnCorra
> `ud_depparse.py` - used for converting `data.txt` to `test_ud.json`. Not required if UD annotation of test already available. 

### Instructions
Steps 1-3 are for setting up before getting started.
Step 4 is for actual conversion
Step 5 is for evalutation of conversion

#### Step 1
> `python jsonify_ac.py abhigyan.txt test_ac.json` converts testing AnnCorra corpus to json

#### Step 2
> `python jsonify_ac.py modi.txt train_ac.json` converts training AnnCorra corpus to json

#### Step 2.5 
> If the UD annotation of testing corpus is not available, make it available. In my case, I have used a state-of-the-art UD parser to compare the results. As such I have used `ud_depparse.py` to convert the data directly to `test_ud.json` without doing step 3.

#### Step 3
> `python jsonify_ud.py abhigyan_ud.txt test_ud.json` converts testing UD corpus to json.

#### Step 4 (Running)
`python train_ud_ac.py` converts UD to AnnCorra finally

#### Step 5 (Evaluation)
`python evaluate.py` gives the accuracy of conversion. In the dataset chosen by me, out of 475 conversions, there were 68 inaccurate allocations and 10 sentences were not properly annotated by the UD tagger used.