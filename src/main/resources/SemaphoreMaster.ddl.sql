CREATE TABLE IF NOT EXISTS `SEMAPHORE_MASTER` (
  `LOCK_KEY` varchar(256) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  PRIMARY KEY (`LOCK_KEY`)
)